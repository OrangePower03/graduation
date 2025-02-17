package com.example.backend.domain.vo.indicator;

import com.example.backend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElderIndicatorVO extends BaseVO {
    private Long id; // 指标的外键id

    private String name;

    private Double value;

    private String unit;

    private String standardRange;

    private Date checkTime;
}
