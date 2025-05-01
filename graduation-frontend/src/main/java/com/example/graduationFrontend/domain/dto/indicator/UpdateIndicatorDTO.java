package com.example.graduationFrontend.domain.dto.indicator;

import com.example.graduationFrontend.domain.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIndicatorDTO extends BaseDTO {
    private Long id;

    private String name;

    private String unit;

    private Double manRangeL;

    private Double manRangeR;

    private Double womanRangeL;

    private Double womanRangeR;

    private Double rangeL;

    private Double rangeR;
}
