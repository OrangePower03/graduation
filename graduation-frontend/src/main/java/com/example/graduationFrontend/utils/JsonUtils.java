package com.example.graduationFrontend.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtils {
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static String objectToJson(Object object) {
        return JSON.toJSONString(object);
    }
}
