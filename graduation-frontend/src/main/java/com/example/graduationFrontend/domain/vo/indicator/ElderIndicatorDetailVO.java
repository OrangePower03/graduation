package com.example.graduationFrontend.domain.vo.indicator;

import com.example.graduationFrontend.domain.vo.BaseVO;
import com.example.graduationFrontend.domain.vo.graph.SuggestionDetailVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElderIndicatorDetailVO extends BaseVO {
    private Long elderId;

    private Long indicatorId;

    private String IndicatorName;

    private Double value;

    private String unit;

    private String standardRange;

    private Integer normal;

    private Date checkTime;

    private SuggestionDetailVO suggestion;

}
