package com.example.backend.domain.vo.graph;

import com.example.backend.domain.graph.FoodNode;
import com.example.backend.domain.vo.BaseVO;
import com.example.backend.utils.object.CollectionUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FoodVO extends BaseVO {
    private String category;

    private List<String> foods;

    public FoodVO(String category, @NotNull List<FoodNode> foodNodes) {
        this.category = category;
        foods = CollectionUtils.mapToList(foodNodes, FoodNode::getName);
    }
}
