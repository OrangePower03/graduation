package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.domain.dto.indicator.ElderIndicatorDTO;
import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.entity.ElderIndicator;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.mapper.ElderIndicatorMapper;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElderIndicatorService extends ServiceImpl<ElderIndicatorMapper, ElderIndicator> implements IService<ElderIndicator> {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IndicatorService indicatorService;

    @VerifyRequestBody
    @Transactional(rollbackFor = Exception.class)
    public @NonNull Integer addElderIndicators(PatchElderIndicatorDTO indicators) {
        SysUser user = sysUserMapper.selectById(indicators.getElderId());
        AssertUtils.nonNull(user, AppHttpCode.USER_NOT_FOUND_ERROR);
        List<ElderIndicator> elderIndicators = new ArrayList<>();
        for (ElderIndicatorDTO elderIndicator : indicators.getElderIndicators()) {
            AssertUtils.nonNull(elderIndicator.getId(), AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
            AssertUtils.nonNull(elderIndicator.getValue(), AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
            AssertUtils.isTrue(indicatorService.existsIndicator(elderIndicator.getId()), AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
            ElderIndicator indicator = BeanCopyUtils.copyBean(elderIndicator, ElderIndicator.class);
            indicator.setElderId(indicators.getElderId());
            indicator.setCheckTime(indicators.getCheckTime());
            elderIndicators.add(indicator);
        }
        return this.saveBatch(elderIndicators) ? elderIndicators.size() : 0;
    }
}

