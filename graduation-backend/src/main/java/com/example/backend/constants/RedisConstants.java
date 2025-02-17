package com.example.backend.constants;

public interface RedisConstants {
     String REDIS_TOKEN_KEY = "login.token:";
     Long REDIS_TOKEN_EXPIRE = 60 * 60 * 24L; // 一天

     String INDICATOR_MAP_KEY = "indicator:";
     Long INDICATOR_MAP_EXPIRE = 60 * 60 * 24L; // 一天
}
