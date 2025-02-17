package com.example.backend.utils.object;

import com.example.backend.utils.AssertUtils;
import com.example.backend.utils.web.AppHttpCode;

import java.util.Objects;

public class ObjectUtils {
    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean nonNull(Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * 存在一些数据不为空，数据全为空返回false
     */
    public static boolean containNonNull(Object... objs) {
        for(Object obj : objs) {
            if(nonNull(obj)) return true;
        }
        return false;
    }

    public static boolean isEquals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    public static <T> T requireNonNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    public static <T> T requireNonNull(T obj, AppHttpCode httpCode) {
        AssertUtils.nonNull(obj, httpCode);
        return obj;
    }

    public static <T> T requireNonNullElse(T obj, T defaultValue) {
        return Objects.requireNonNullElse(obj, defaultValue);
    }
}
