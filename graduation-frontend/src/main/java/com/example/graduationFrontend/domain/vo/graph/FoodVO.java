package com.example.graduationFrontend.domain.vo.graph;


import com.example.graduationFrontend.domain.vo.BaseVO;
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

}
