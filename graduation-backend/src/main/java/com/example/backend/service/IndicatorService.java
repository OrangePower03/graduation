package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.RedisConstants;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndicatorService extends ServiceImpl<IndicatorMapper, Indicator> implements IService<Indicator> {
    private final Map<String, IndicatorVO> indicatorMap = new HashMap<>();

    @Autowired
    private HashRedisUtils redisCache;

    public @NonNull List<IndicatorVO> listIndicator() {
        Map<String, Object> map = redisCache.getWithExpire(RedisConstants.INDICATOR_MAP_KEY, RedisConstants.INDICATOR_MAP_EXPIRE);
        return ObjectUtils.isNull(map) ? getByDatabaseAndCache() :
                map.values().stream()
                    .map(IndicatorVO.class::cast)
                    .collect(Collectors.toList());
    }

    public boolean existsIndicator(@NonNull Long id) {
        Map<String, Object> map = redisCache.get(RedisConstants.INDICATOR_MAP_KEY);
        if (ObjectUtils.nonNull(map)) {
            return map.containsKey(id.toString());
        } else {
            getByDatabaseAndCache();
            return indicatorMap.containsKey(id.toString());
        }
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

