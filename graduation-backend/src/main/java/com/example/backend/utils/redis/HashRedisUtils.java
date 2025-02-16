package com.example.backend.utils.redis;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

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

    @SuppressWarnings("unchecked")
    public <T> T getHash(String key, String hashKey) {
        return (T) getOperations().get(key, hashKey);
    }
}
