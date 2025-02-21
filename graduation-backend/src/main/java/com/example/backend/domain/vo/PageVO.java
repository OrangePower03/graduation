package com.example.backend.domain.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.domain.entity.BaseEntity;
import com.example.backend.domain.vo.indicator.ElderBaseIndicatorVO;
import com.example.backend.utils.bean.BeanCopyUtils;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public <E> PageVO(Page<E> page, Class<T> voClass) {
        this.total = Math.toIntExact(page.getTotal());
        this.rows = page.getRecords().stream()
                .map(record -> BeanCopyUtils.copyBean((BaseEntity) record, voClass))
                .collect(Collectors.toList());
    }

}
