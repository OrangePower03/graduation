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
    public final static int DEFAULT_PAGE_SIZE = 10;

    List<T> rows;

    int total;

    int current;

    int pageSize = 10;
}
