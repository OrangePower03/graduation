package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.UserConstants;
import com.example.backend.domain.dto.indicator.ElderIndicatorDTO;
import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.entity.ElderIndicator;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.domain.vo.PageVO;
import com.example.backend.domain.vo.graph.SuggestionDetailVO;
import com.example.backend.domain.vo.indicator.ElderBaseIndicatorVO;
import com.example.backend.domain.vo.indicator.ElderIndicatorDetailVO;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.domain.vo.indicator.PatchElderIndicatorVO;
import com.example.backend.mapper.ElderIndicatorMapper;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.PageUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.DateUtils;
import com.example.backend.utils.security.SecurityUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ElderIndicatorService extends ServiceImpl<ElderIndicatorMapper, ElderIndicator> implements IService<ElderIndicator> {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private Neo4jService neo4jService;

    @VerifyRequestBody
    @Transactional(rollbackFor = Exception.class)
    public @NonNull Integer addElderIndicators(PatchElderIndicatorDTO indicators) {
        SysUser user = sysUserMapper.selectById(indicators.getElderId());
        AssertUtils.nonNull(user, AppHttpCode.USER_NOT_FOUND_ERROR);
        List<ElderIndicator> elderIndicators = new ArrayList<>();
        for (ElderIndicatorDTO elderIndicator : indicators.getElderIndicators()) {
            AssertUtils.nonNull(elderIndicator.getIndicatorId(), AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
            AssertUtils.nonNull(elderIndicator.getValue(), AppHttpCode.REQUEST_DATA_FIELD_IS_NULL);
            Integer normalStatus = indicatorService.getNormalStatus(user.getSex(), elderIndicator);
            ElderIndicator indicator = BeanCopyUtils.copyBean(elderIndicator, ElderIndicator.class);
            indicator.setElderId(indicators.getElderId());
            indicator.setCheckTime(indicators.getCheckTime());
            indicator.setNormal(normalStatus);
            elderIndicators.add(indicator);
        }
        return this.saveBatch(elderIndicators) ? elderIndicators.size() : 0;
    }

    public @NonNull PatchElderIndicatorVO getElderIndicators(Long elderId, Date startTime) {
        judgePermission(elderId);
        LambdaQueryWrapper<ElderIndicator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ElderIndicator::getElderId, elderId);
        wrapper.ge(DateUtils.nonNull(startTime), ElderIndicator::getCheckTime, startTime);
        wrapper.orderByDesc(ElderIndicator::getCheckTime);
        Page<ElderIndicator> page = this.page(PageUtils.getPage(), wrapper);
//        List<ElderBaseIndicatorVO> collect = page.getRecords().stream()
//                .map(record -> BeanCopyUtils.copyBean(record, ElderBaseIndicatorVO.class))
//                .collect(Collectors.toList());
//        PageVO<ElderBaseIndicatorVO> baseIndicator = new PageVO<>(collect, Math.toIntExact(page.getTotal()));
        PageVO<ElderBaseIndicatorVO> baseIndicator = new PageVO<>(page, ElderBaseIndicatorVO.class);
        return new PatchElderIndicatorVO(elderId, baseIndicator);
    }

    public @NonNull List<ElderIndicatorDetailVO> getElderIndicatorDetail(Long elderId, Date checkTime) {
        judgePermission(elderId);
        LambdaQueryWrapper<ElderIndicator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ElderIndicator::getElderId, elderId);
        wrapper.eq(ElderIndicator::getCheckTime, checkTime);
        List<ElderIndicator> elderIndicators = this.list(wrapper);
        List<ElderIndicatorDetailVO> res = BeanCopyUtils.copyBeans(elderIndicators, ElderIndicatorDetailVO.class);
        for (ElderIndicatorDetailVO elderIndicator : res) {
            Long indicatorId = elderIndicator.getIndicatorId();
            IndicatorVO indicator = indicatorService.getIndicator(indicatorId);
            elderIndicator.setIndicatorName(indicator.getName());
            elderIndicator.setUnit(indicator.getUnit());
            elderIndicator.setStandardRange(indicator.getStandardRange());
            if (!UserConstants.USER_INDICATOR_NORMAL.equals(elderIndicator.getNormal())) {
                SuggestionDetailVO suggestion = neo4jService.getSuggestion(indicatorId, elderIndicator.getNormal());
                elderIndicator.setSuggestion(suggestion);
            }
        }
        return res;
    }

    private void judgePermission(Long elderId) {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isYoungster()) {
            AssertUtils.isTrue(sysUserMapper.containsRelations(userId, elderId) > 0, AppHttpCode.PERMISSION_DENIED_ERROR);
        } else {
            AssertUtils.isEquals(userId, elderId, AppHttpCode.PERMISSION_DENIED_ERROR);
        }
    }
}

