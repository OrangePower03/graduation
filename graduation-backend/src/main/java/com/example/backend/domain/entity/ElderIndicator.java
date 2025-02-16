package com.example.backend.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("elder_indicator")
public class ElderIndicator extends BaseEntity {
    //老人id
    private Long elderId;

    //指标id
    private Long indicatorId;

    //指标值
    private BigDecimal value;
}

