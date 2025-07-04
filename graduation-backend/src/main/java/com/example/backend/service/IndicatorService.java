package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.annotation.VerifyRequestBody;
import com.example.backend.constants.RedisConstants;
import com.example.backend.constants.UserConstants;
import com.example.backend.domain.dto.indicator.ElderIndicatorDTO;
import com.example.backend.domain.dto.indicator.IndicatorDTO;
import com.example.backend.domain.entity.Indicator;
import com.example.backend.domain.vo.indicator.IndicatorVO;
import com.example.backend.mapper.IndicatorMapper;
import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.bean.BeanCopyUtils;
import com.example.backend.utils.object.CollectionUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.redis.HashRedisUtils;
import com.example.backend.utils.web.AppHttpCode;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public @NonNull IndicatorVO getIndicator(@NonNull Long id) {
        IndicatorVO res;
        if (ObjectUtils.nonNull(res = indicatorMap.get(id.toString()))) return res;
        Map<String, Object> map = redisCache.get(RedisConstants.INDICATOR_MAP_KEY);
        if (CollectionUtils.notEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                indicatorMap.put(entry.getKey(), (IndicatorVO) entry.getValue());
            }
        } else {
            getByDatabaseAndCache();
        }
        res = indicatorMap.get(id.toString());
        AssertUtils.nonNull(res, AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
        return res;
    }

    @VerifyRequestBody
    public IndicatorVO addIndicator(IndicatorDTO indicatorDTO) {
        String range = indicatorDTO.getStandardRange();
        boolean validFormat = range.matches("(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*);(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*)") ||
               range.matches("(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*)");
        AssertUtils.isTrue(validFormat, AppHttpCode.INDICATOR_RANGE_ERROR);
        Indicator indicator = BeanCopyUtils.copyBean(indicatorDTO, Indicator.class);
        save(indicator);
        removeCacheIndicator();
        return BeanCopyUtils.copyBean(indicator, IndicatorVO.class);
    }

    @VerifyRequestBody
    public IndicatorVO updateIndicator(IndicatorDTO indicatorDTO, Long id) {
        String range = indicatorDTO.getStandardRange();
        boolean validFormat = range.matches("(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*);(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*)") ||
               range.matches("(\\d+\\.?\\d*) ~ (\\d+\\.?\\d*)");
        AssertUtils.isTrue(validFormat, AppHttpCode.INDICATOR_RANGE_ERROR);
        Indicator indicator = BeanCopyUtils.copyBean(indicatorDTO, Indicator.class);
        indicator.setId(id);
        updateById(indicator);
        removeCacheIndicator();
        return BeanCopyUtils.copyBean(indicator, IndicatorVO.class);
    }

    public IndicatorVO removeIndicator(Long id) {
        AssertUtils.nonNull(id, AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
        Indicator indicator = getById(id);
        AssertUtils.nonNull(id, AppHttpCode.INDICATOR_NOT_FOUND_ERROR);
        removeById(id);
        removeCacheIndicator();
        return BeanCopyUtils.copyBean(indicator, IndicatorVO.class);
    }

    private void removeCacheIndicator() {
        redisCache.remove(RedisConstants.INDICATOR_MAP_KEY);
        indicatorMap.clear();
    }



    public Integer getNormalStatus(Integer elderSex, ElderIndicatorDTO elderIndicator) {
        IndicatorVO indicator = getIndicator(elderIndicator.getIndicatorId());
        AssertUtils.isTrue(UserConstants.USER_SEX_MAN.equals(elderSex) || UserConstants.USER_SEX_WOMAN.equals(elderSex), AppHttpCode.USER_SEX_ERROR);
        String[] sexIndicator = indicator.getStandardRange().split(";");
        String indicatorRange = sexIndicator.length == 2 ? sexIndicator[elderSex] : sexIndicator[0];
        String[] indicatorArr = indicatorRange.split("~");
        Double value = elderIndicator.getValue();
        double lowest = Double.parseDouble(indicatorArr[0].strip().substring(2));
        double highest = Double.parseDouble(indicatorArr[1].strip());
        if (lowest > value) {
            return UserConstants.USER_INDICATOR_LOW;
        } else if (value > highest) {
            return UserConstants.USER_INDICATOR_HIGH;
        } else {
            return UserConstants.USER_INDICATOR_NORMAL;
        }
    }

    private List<IndicatorVO> getByDatabaseAndCache() {
        indicatorMap.clear();
        List<IndicatorVO> indicators = BeanCopyUtils.copyBeans(list(), IndicatorVO.class);
        for (IndicatorVO indicator : indicators) {
            String range = indicator.getStandardRange();
            if (range.contains(";")) {
                String[] ranges = range.split(";");
                range = "男：" + ranges[0] + "; 女：" + ranges[1];
            }
            indicator.setStandardRange(range);
            indicatorMap.put(indicator.getId().toString(), indicator);
        }
        redisCache.putAllWithExpire(RedisConstants.INDICATOR_MAP_KEY, indicatorMap, RedisConstants.INDICATOR_MAP_EXPIRE);
        return indicators;
    }

    public String getStandardRange(Integer sex, String standardRange) {
        if (standardRange.contains(";")) {
            return standardRange.split(";")[sex];
        }
        return standardRange;
    }
}

