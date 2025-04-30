package com.example.graduationFrontend.domain.dto.indicator;

import com.example.graduationFrontend.domain.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorDTO extends BaseDTO {
    private String name;

    private String unit;

    private String standardRange;
}
