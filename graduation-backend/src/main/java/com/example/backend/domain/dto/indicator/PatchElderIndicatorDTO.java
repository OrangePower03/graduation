package com.example.backend.domain.dto.indicator;

import com.example.backend.domain.dto.BaseDTO;
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

    private Date checkTime;

    private List<ElderIndicatorDTO> elderIndicators;

}
