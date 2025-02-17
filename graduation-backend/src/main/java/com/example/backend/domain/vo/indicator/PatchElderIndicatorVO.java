package com.example.backend.domain.vo.indicator;

import com.example.backend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatchElderIndicatorVO extends BaseVO {
    private Long elderId;

    private List<ElderIndicatorVO> elderIndicators;

    private List<String> suggestions;
}
