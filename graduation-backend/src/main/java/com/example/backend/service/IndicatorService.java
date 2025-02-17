package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.entity.Indicator;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.mapper.IndicatorMapper;
import com.example.backend.utils.bean.BeanCopyUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndicatorService extends ServiceImpl<IndicatorMapper, Indicator> implements IService<Indicator> {

    public @NonNull List<IndicatorVO> listIndicator() {
        return BeanCopyUtils.copyBeans(list(), IndicatorVO.class);
    }

    public void addElderIndicators(PatchElderIndicatorDTO indicators) {
        
    }
}

