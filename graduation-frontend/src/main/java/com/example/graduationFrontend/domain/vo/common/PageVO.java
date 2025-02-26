package com.example.graduationFrontend.domain.vo.common;


import com.example.graduationFrontend.domain.vo.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class PageVO<T extends BaseVO> extends BaseVO {
    List<T> rows;

    int total;
}
