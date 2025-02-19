package com.example.backend.controller;

import com.example.backend.utils.web.ResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.backend.utils.file.FileUtils.*;

@RestController
@RequestMapping("/common")
public class CommonController extends BaseController{


    @PostMapping("/graph")
    public ResponseResult<Void> graph() {
        try {
            String[] str = new String(getResourcesInputStream("data.txt").readAllBytes()).split("\r\n");
            Map<String, Map<String, String>> indicatorMap = getDataByPrefixAndFormat(str, "-");
            Map<String, Map<String, String>> symptomMap = getDataByPrefixAndFormat(str, "+");
            Map<String, Map<String, String>> foodMap = getDataByPrefixAndFormat(str, "/");

            for (Map.Entry<String, Map<String, String>> entry : indicatorMap.entrySet()) {
                String indicator = entry.getKey();
                String symptom1 = entry.getValue().get("低");
                String symptom2 = entry.getValue().get("高");
            }

            for (Map.Entry<String, Map<String, String>> entry : symptomMap.entrySet()) {
                String symptom = entry.getKey();
                String recommend = entry.getValue().get("宜");
                String unrecommend = entry.getValue().get("不宜");
                String realSymptom = entry.getValue().get("症状");
                String suggestion = entry.getValue().get("建议");
            }

            Map<String, String> foods = foodMap.get("食物类别");
            for (Map.Entry<String, String> entry : foods.entrySet()) {
                String foodCategory = entry.getKey();
                String[] food = entry.getValue().split("、");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ok();
    }
}
