package com.example.backend.controller;

import com.example.backend.domain.entity.Indicator;
import com.example.backend.domain.graph.FoodCategoryNode;
import com.example.backend.domain.graph.FoodNode;
import com.example.backend.domain.graph.IndicatorNode;
import com.example.backend.domain.graph.SymptomNode;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.service.IndicatorService;
import com.example.backend.service.Neo4jService;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.object.CollectionUtils;
import com.example.backend.utils.web.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.backend.constants.Neo4jConstants.*;
import static com.example.backend.utils.file.FileUtils.*;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController extends BaseController {
    @Autowired
    private Neo4jService neo4jService;

    @Autowired
    private IndicatorService indicatorService;

    @PostMapping("/graph/{delete}")
    public ResponseResult<Void> graph(@PathVariable Boolean delete) {
        try {
            if (delete) {
                neo4jService.deleteAll();
            }
            String[] str = new String(getResourcesInputStream("data.txt").readAllBytes()).split("\r\n");
            Map<String, Map<String, String>> indicatorMap = getDataByPrefixAndFormat(str, "-");
            Map<String, Map<String, String>> symptomMap = getDataByPrefixAndFormat(str, "+");
            Map<String, Map<String, String>> foodMap = getDataByPrefixAndFormat(str, "/");

            Map<String, String> foods = foodMap.get("食物类别");
            for (Map.Entry<String, String> entry : foods.entrySet()) {
                String foodCategory = entry.getKey();
                Set<FoodNode> food = Arrays.stream(entry.getValue().split("、")).map(FoodNode::new).collect(Collectors.toSet());
                FoodCategoryNode foodCategoryNode = new FoodCategoryNode(foodCategory);
                neo4jService.addNodes(Set.of(foodCategoryNode));
                neo4jService.addNodes(food);
                neo4jService.addRelationsById(foodCategoryNode, food, FOOD_CATEGORY_FOOD_RELATION);
            }
            log.info("食物和食物分类节点关系添加完成");

            for (Map.Entry<String, Map<String, String>> entry : symptomMap.entrySet()) {
                String symptom = entry.getKey();
                String recommend = entry.getValue().get("宜");
                String unrecommend = entry.getValue().get("不宜");
                String realSymptom = entry.getValue().get("症状");
                String suggestion = entry.getValue().get("建议");
                SymptomNode symptomNode = new SymptomNode(symptom, realSymptom, suggestion);
                neo4jService.addNodes(Set.of(symptomNode));
                neo4jService.addRelationsById(symptomNode, Arrays.stream(recommend.split("、")).map(FoodCategoryNode::new).collect(Collectors.toSet()), SYMPTOM_FOOD_CATEGORY_RECOMMEND_RELATION);
                neo4jService.addRelationsById(symptomNode, Arrays.stream(unrecommend.split("、")).map(FoodCategoryNode::new).collect(Collectors.toSet()), SYMPTOM_FOOD_CATEGORY_UNRECOMMEND_RELATION);
            }
            log.info("食物分类和症状节点关系添加完成");

            for (Map.Entry<String, Map<String, String>> entry : indicatorMap.entrySet()) {
                String indicator = entry.getKey();
                String symptomLow = entry.getValue().get("低");
                String symptomHigh = entry.getValue().get("高");

                Map<String, Long> indicatorNameIdMap = CollectionUtils.toMap(indicatorService.listIndicator(), IndicatorVO::getName, IndicatorVO::getId);
                Long id = indicatorNameIdMap.get(indicator);
                AssertUtils.nonNull(id, indicator + " 该指标不存在");
                IndicatorNode indicatorNode = new IndicatorNode(id.toString(), indicator);
                neo4jService.addNodes(Set.of(indicatorNode));
                SymptomNode symptomNode = new SymptomNode();
                symptomNode.setName(symptomLow);
                neo4jService.addRelationsById(indicatorNode, Set.of(symptomNode), INDICATOR_SYMPTOM_LOW_RELATION);
                symptomNode.setName(symptomHigh);
                neo4jService.addRelationsById(indicatorNode, Set.of(symptomNode), INDICATOR_SYMPTOM_HIGH_RELATION);
            }
            log.info("症状和指标节点关系添加完成");
        } catch (Exception e) {
            neo4jService.deleteAll();
            throw new RuntimeException(e);
        }
        return ok();
    }
}
