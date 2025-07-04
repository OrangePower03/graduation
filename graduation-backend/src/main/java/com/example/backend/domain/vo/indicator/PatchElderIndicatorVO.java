package com.example.backend.domain.vo.indicator;

import com.example.backend.domain.vo.BaseVO;
import com.example.backend.domain.vo.PageVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatchElderIndicatorVO extends BaseVO {
    private Long elderId;

    PageVO<ElderBaseIndicatorVO> allIndicator;

}
