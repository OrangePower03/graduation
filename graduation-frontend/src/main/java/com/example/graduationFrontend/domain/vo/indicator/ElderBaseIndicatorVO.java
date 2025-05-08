package com.example.graduationFrontend.domain.vo.indicator;

import com.example.graduationFrontend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElderBaseIndicatorVO extends BaseVO {
    Long id;

    String checkTime;

    Integer normal;

    Double value;

    Long indicatorId;

    String indicatorName;
}
