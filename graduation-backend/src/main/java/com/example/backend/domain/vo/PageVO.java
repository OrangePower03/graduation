package com.example.backend.domain.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class PageVO<T extends BaseVO> extends BaseVO {
    List<T> rows;

    int total;

    public PageVO() {
        rows = new ArrayList<>();
        total = 0;
    }
}
