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
public class ElderBaseIndicatorVO extends BaseVO {
    Long id; // 老年人指标主键

    Date checkTime;

    Integer normal; // 所有的指标是否正常

    Double value;

    Long indicatorId;

    String indicatorName;
}
