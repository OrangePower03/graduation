package com.example.backend.domain.dto.indicator;

import com.example.backend.domain.dto.BaseDTO;
import com.example.backend.utils.object.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PatchElderIndicatorDTO extends BaseDTO {
    private Long elderId;

    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date checkTime;

    private List<ElderIndicatorDTO> elderIndicators;

}
