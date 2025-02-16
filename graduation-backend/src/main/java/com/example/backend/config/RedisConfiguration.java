package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        // 以下是默认的标准redis模板类，可以去看redis的自动配置
        RedisTemplate<String,Object> redis=new RedisTemplate<>();
        redis.setConnectionFactory(factory);

        redis.setKeySerializer(new StringRedisSerializer());  //key一般都是String
        redis.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redis.setHashKeySerializer(new StringRedisSerializer());
        redis.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));

        return redis;
    }



}
