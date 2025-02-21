package com.example.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

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
    private Double value;

    //指标是否正常，正常为0，指标过高为1，过低为2
    private Integer normal;

    //体检时间
    private Date checkTime;
}

