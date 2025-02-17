package com.example.backend.domain.dto.indicator;

import com.example.backend.domain.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElderIndicatorDTO extends BaseDTO {
    private Long id; // 指标的外键id

    private Double value;
}
