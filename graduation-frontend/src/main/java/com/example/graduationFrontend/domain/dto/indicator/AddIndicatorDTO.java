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
public class AddIndicatorDTO extends BaseDTO {
    private String name;

    private String unit;

    private Double manStandardRangeL;

    private Double manStandardRangeR;

    private Double womanStandardRangeL;

    private Double womanStandardRangeR;
}
