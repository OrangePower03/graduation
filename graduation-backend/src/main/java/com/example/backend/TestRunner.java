package com.example.backend;

import com.example.backend.controller.CommonController;
import com.example.backend.domain.entity.Indicator;
import com.example.backend.mapper.IndicatorMapper;
import com.example.backend.service.IndicatorService;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<Indicator> indicators = indicatorService.list();
//        for (Indicator indicator : indicators) {
//            System.out.println(indicator.getName() + "这个数值过低和过高的危害吗？可以通过什么方式来补救呢，可以结合饮食和生活习性讲解");
//        }
        controller.graph(true);

    }
}
/*
宜：
不宜：
症状：
建议：
*/
