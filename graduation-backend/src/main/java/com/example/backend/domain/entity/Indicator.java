package com.example.backend.domain.entity;

import java.util.Date;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (Indicator)表实体类
 *
 * @author makejava
 * @since 2025-02-15 21:12:54
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("indicator")
public class Indicator extends BaseEntity {
    //指标名称
    private String name;

    //指标的单位
    private String unit;

    //指标的标准范围
    private String standardRange;
}

