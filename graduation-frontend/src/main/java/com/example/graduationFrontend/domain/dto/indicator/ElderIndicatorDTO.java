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
public class ElderIndicatorDTO extends BaseDTO {
    private Long indicatorId; // 指标的外键id

    private Double value;
}
