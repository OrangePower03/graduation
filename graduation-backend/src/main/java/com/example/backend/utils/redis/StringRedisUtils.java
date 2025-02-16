package com.example.backend.utils.redis;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@DependsOn("redisUtils")
@Component
public class StringRedisUtils extends RedisUtils {
    private ValueOperations<String, Object> operations;

    private ValueOperations<String, Object> getOperations() {
        return operations != null ? operations :
              (operations = redisTemplate.opsForValue());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) getOperations().get(key);
    }

    public void set(String key, Object value) {
        getOperations().set(key, value);
    }

    public boolean setIfAbsent(String key, Object value) {
        return Boolean.TRUE.equals(getOperations().setIfAbsent(key, value));
    }

    public void setWithExpire(String key, Object value, long timeout) {
        setWithExpire(key, value, timeout, TimeUnit.SECONDS);
    }

    public void setWithExpire(String key, Object value, long timeout, TimeUnit timeUnit) {
        getOperations().set(key, value, timeout, timeUnit);
    }

    public Long increase(String key) {
        return getOperations().increment(key);
    }
}
