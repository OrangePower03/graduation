package com.example.graduationFrontend.domain.vo.graph;

import com.example.graduationFrontend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDetailVO extends BaseVO {

    private String symptomName; // 症状名，如高血压

    private String induced; // 诱发的症状，例如糖尿病

    private String lifeSuggestion; // 生活建议

    private List<FoodVO> recommendFoods; // 推荐食物

    private List<FoodVO> unrecommendFoods; // 不推荐食物

    private String chatModelSuggestion; // 大模型生成的建议
}
