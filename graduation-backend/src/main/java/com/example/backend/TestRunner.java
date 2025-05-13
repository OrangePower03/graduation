package com.example.backend;

import com.example.backend.constants.UserConstants;
import com.example.backend.controller.CommonController;
import com.example.backend.domain.entity.Indicator;
import com.example.backend.domain.vo.graph.SuggestionDetailVO;
import com.example.backend.mapper.IndicatorMapper;
import com.example.backend.service.ChatModelService;
import com.example.backend.service.IndicatorService;
import com.example.backend.service.Neo4jService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRunner implements ApplicationRunner {
    @Autowired
    IndicatorService indicatorService;

    @Autowired
    private CommonController controller;

    @Autowired
    private Neo4jService neo4jService;

    @Autowired
    private ChatModelService chatModelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        controller.graph(false);
//        System.out.println(chatModelService.generate("红细胞数（RBC）", "高"));
//        List<Indicator> indicators = indicatorService.list();
//        for (Indicator indicator : indicators) {
//            System.out.println(indicator.getName() + "这个数值过低和过高的危害吗？可以通过什么方式来补救呢，可以结合饮食和生活习性讲解");
//        }
//        controller.graph(true);
//        SuggestionDetailVO suggestion = neo4jService.getSuggestion(1L, UserConstants.USER_INDICATOR_LOW);
//        System.out.println(suggestion);
    }
}
/*
宜：
不宜：
症状：
建议：
*/
