package com.example.graduationFrontend.domain.vo.indicator;

import com.example.graduationFrontend.domain.vo.BaseVO;
import com.example.graduationFrontend.domain.vo.common.PageVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatchElderIndicatorVO extends BaseVO {
    private Long elderId;

    PageVO<ElderBaseIndicatorVO> allIndicator;

}
