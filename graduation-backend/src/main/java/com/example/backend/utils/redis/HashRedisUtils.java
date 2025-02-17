package com.example.backend.utils.redis;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.Map;

@DependsOn("redisUtils")
@Component
public class HashRedisUtils extends RedisUtils {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Object> operations;

    private HashOperations<String, String, Object> getOperations() {
        return operations != null ? operations :
              (operations = redisTemplate.opsForHash());
    }

    public boolean putWithExpire(String key, String hashKey, Object value, long expireTime) {
        put(key, hashKey, value);
        return expire(key, expireTime);
    }

    public void put(String key, String hashKey, Object value) {
        getOperations().put(key, hashKey, value);
    }

    public <T> void putAll(String key, Map<String, T> map) {
        getOperations().putAll(key, map);
    }

    public <T> void putAllWithExpire(String key, Map<String, T> map, long expireTime) {
        getOperations().putAll(key, map);
        expire(key, expireTime);
    }

    @SuppressWarnings("unchecked")
    public <T> T getHash(String key, String hashKey) {
        return (T) getOperations().get(key, hashKey);
    }

    public Map<String, Object> get(String key) {
        return getOperations().entries(key);
    }

    public Map<String, Object> getWithExpire(String key, long expireTime) {
        Map<String, Object> val = getOperations().entries(key);
        expire(key, expireTime);
        return val;
    }
}
