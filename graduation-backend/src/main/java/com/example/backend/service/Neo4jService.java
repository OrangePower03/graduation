package com.example.backend.service;

import com.example.backend.constants.UserConstants;
import com.example.backend.domain.graph.*;
import com.example.backend.domain.vo.graph.FoodVO;
import com.example.backend.domain.vo.graph.SuggestionDetailVO;
import com.example.backend.exception.GlobalException;
import com.example.backend.utils.web.AppHttpCode;
import org.neo4j.cypherdsl.core.Node;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.example.backend.constants.Neo4jConstants.*;
import static org.neo4j.cypherdsl.core.Cypher.*;

@Service
public class Neo4jService {
    @Autowired
    private Driver neo4jDriver;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int addNodes(Set<? extends BaseNode> nodes) {
        return neo4jTemplate.saveAll(nodes).size();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int addRelationsById(BaseNode parentNode, Set<? extends BaseNode> childrenNodes, String relationName) {

        Node parent = node(parentNode.getNodeName()).named("parent").withProperties(Map.of(parentNode.getIdKey(), parentNode.getIdValue()));
        List<String> cql = childrenNodes.stream().map(childrenNode -> {
            Node child = node(childrenNode.getNodeName()).named("child").withProperties(Map.of(childrenNode.getIdKey(), childrenNode.getIdValue()));
            return match(parent).match(child)
                    .create(parent.relationshipTo(child, relationName))
                    .build().getCypher();
        }).collect(Collectors.toList());
        return executeCql(cql);
    }

    // 根据症状来获取建议
    public SuggestionDetailVO getSuggestion(Long indicatorId, Integer normal) {
        SuggestionDetailVO res = new SuggestionDetailVO();
        String symptomRelation;
        if (UserConstants.USER_INDICATOR_LOW.equals(normal)) {
            symptomRelation = INDICATOR_SYMPTOM_LOW_RELATION;
        } else if (UserConstants.USER_INDICATOR_HIGH.equals(normal)) {
            symptomRelation = INDICATOR_SYMPTOM_HIGH_RELATION;
        } else {
            throw new IllegalArgumentException("normal 参数错误");
        }

        String symptomNodeName = "symptom";
        Node indicatorNode = node(INDICATOR_NODE).withProperties(Map.of(IndicatorNode.ID, indicatorId.toString())).named("indicator");
        Node symptomNode = node(SYMPTOM_NODE).named(symptomNodeName);
        String symptomCql = match(indicatorNode.relationshipTo(symptomNode, symptomRelation))
                .returning(symptomNodeName)
                .build().getCypher();
        SymptomNode symptomNodeValue = getOne(symptomCql, SymptomNode.class, () -> {
            throw new GlobalException(AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
        });
        res.setInduced(symptomNodeValue.getInduced());
        res.setLifeSuggestion(symptomNodeValue.getSuggestion());
        res.setSymptomName(symptomNodeValue.getName());
        res.setRecommendFoods(new ArrayList<>());
        res.setUnrecommendFoods(new ArrayList<>());

        String foodCategoryNodeName = "foodCategory";
        symptomNode = symptomNode.withProperties(Map.of(SymptomNode.NAME, symptomNodeValue.getName()));
        Node foodCategoryNode = node(FOOD_CATEGORY_NODE).named(foodCategoryNodeName);
        String recommendFoodCategoryCql = match(symptomNode.relationshipTo(foodCategoryNode, SYMPTOM_FOOD_CATEGORY_RECOMMEND_RELATION))
                .returning(foodCategoryNodeName)
                .build().getCypher();
        List<FoodCategoryNode> foodCategoryNodes = list(recommendFoodCategoryCql, FoodCategoryNode.class);
        String foodNodeName = "food";
        Node foodNode = node(FOOD_NODE).named(foodNodeName);
        for (FoodCategoryNode foodCategoryNodeValue : foodCategoryNodes) {
            String categoryName = foodCategoryNodeValue.getName();
            foodCategoryNode = node(FOOD_CATEGORY_NODE)
                    .withProperties(Map.of(FoodCategoryNode.NAME, categoryName))
                    .named(foodCategoryNodeName);
            String recommendFoodCql = match(foodCategoryNode.relationshipTo(foodNode, FOOD_CATEGORY_FOOD_RELATION))
                .returning(foodNodeName)
                .build().getCypher();
            List<FoodNode> foods = list(recommendFoodCql, FoodNode.class);
            res.getRecommendFoods().add(new FoodVO(categoryName, foods));
        }

        foodCategoryNode = node(FOOD_CATEGORY_NODE).named(foodCategoryNodeName);
        String unrecommendFoodCategoryCql = match(symptomNode.relationshipTo(foodCategoryNode, SYMPTOM_FOOD_CATEGORY_UNRECOMMEND_RELATION))
                .returning(foodCategoryNodeName)
                .build().getCypher();
        foodCategoryNodes = list(unrecommendFoodCategoryCql, FoodCategoryNode.class);
        for (FoodCategoryNode foodCategoryNodeValue : foodCategoryNodes) {
            String categoryName = foodCategoryNodeValue.getName();
            foodCategoryNode = node(FOOD_CATEGORY_NODE)
                    .withProperties(Map.of(FoodCategoryNode.NAME, categoryName))
                    .named(foodCategoryNodeName);
            String recommendFoodCql = match(foodCategoryNode.relationshipTo(foodNode, FOOD_CATEGORY_FOOD_RELATION))
                .returning(foodNodeName)
                .build().getCypher();
            List<FoodNode> foods = list(recommendFoodCql, FoodNode.class);
            res.getUnrecommendFoods().add(new FoodVO(categoryName, foods));
        }
        return res;
    }

    public <T> T getOne(String cql, Class<T> clazz, Supplier<T> exceptionHandler) {
        return neo4jTemplate.findOne(cql, Map.of(), clazz).orElseGet(exceptionHandler);
    }

    public <T> List<T> list(String cql, Class<T> clazz) {
        return neo4jTemplate.findAll(cql, clazz);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public int executeCql(List<String> cql) {
        try(Session session = neo4jDriver.session()) {
            for (String c : cql) {
                session.run(c);
            }
            return cql.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "neo4jTransactionManager")
    public void deleteAll() {
        String cql = "MATCH (n) DETACH DELETE n";
        executeCql(List.of(cql));
    }


}
