package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.RedisConstants;
import com.example.backend.constants.UserConstants;
import com.example.backend.domain.dto.indicator.ElderIndicatorDTO;
import com.example.backend.domain.dto.indicator.PatchElderIndicatorDTO;
import com.example.backend.domain.entity.ElderIndicator;
import com.example.backend.domain.entity.Indicator;
import com.example.backend.domain.entity.SysUser;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.mapper.IndicatorMapper;
import com.example.backend.mapper.SysUserMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.CollectionUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.redis.HashRedisUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class IndicatorService extends ServiceImpl<IndicatorMapper, Indicator> implements IService<Indicator> {
    private final Map<String, IndicatorVO> indicatorMap = new ConcurrentHashMap<>();

    @Autowired
    private HashRedisUtils redisCache;

    public @NonNull List<IndicatorVO> listIndicator() {
        Map<String, Object> map = redisCache.getWithExpire(RedisConstants.INDICATOR_MAP_KEY, RedisConstants.INDICATOR_MAP_EXPIRE);
        return CollectionUtils.isEmpty(map) ? getByDatabaseAndCache() :
                map.values().stream()
                    .map(IndicatorVO.class::cast)
                    .collect(Collectors.toList());
    }

    public boolean existsIndicator(@NonNull Long id) {
        return ObjectUtils.nonNull(getIndicator(id));
    }

    @Nullable
    public IndicatorVO getIndicator(@NonNull Long id) {
        IndicatorVO res;
        if (ObjectUtils.nonNull(res = indicatorMap.get(id.toString()))) return res;
        Map<String, Object> map = redisCache.get(RedisConstants.INDICATOR_MAP_KEY);
        if (ObjectUtils.nonNull(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                indicatorMap.put(entry.getKey(), (IndicatorVO) entry.getValue());
            }
        } else {
            getByDatabaseAndCache();
        }
        return indicatorMap.get(id.toString());
    }

    public Integer isNormal(Integer elderSex, ElderIndicatorDTO elderIndicator) {
        IndicatorVO indicator = getIndicator(elderIndicator.getIndicatorId());
        AssertUtils.nonNull(indicator, AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
        AssertUtils.isTrue(UserConstants.USER_SEX_MAN.equals(elderSex) || UserConstants.USER_SEX_WOMAN.equals(elderSex), AppHttpCode.USER_SEX_ERROR);
        assert indicator != null;
        String[] sexIndicator = indicator.getStandardRange().split(";");
        String indicatorRange = sexIndicator.length == 2 ? sexIndicator[elderSex] : sexIndicator[0];
        String[] indicatorArr = indicatorRange.split(";");
        Double value = elderIndicator.getValue();
        return Double.parseDouble(indicatorArr[0].strip()) < value && value < Double.parseDouble(indicatorArr[1].strip()) ?
                UserConstants.USER_BODY_NORMAL : UserConstants.USER_BODY_ABNORMALITY;
    }

    private List<IndicatorVO> getByDatabaseAndCache() {
        indicatorMap.clear();
        List<IndicatorVO> indicators = BeanCopyUtils.copyBeans(list(), IndicatorVO.class);
        for (IndicatorVO indicator : indicators) {
            indicatorMap.put(indicator.getId().toString(), indicator);
        }
        redisCache.putAllWithExpire(RedisConstants.INDICATOR_MAP_KEY, indicatorMap, RedisConstants.INDICATOR_MAP_EXPIRE);
        return indicators;
    }


}

