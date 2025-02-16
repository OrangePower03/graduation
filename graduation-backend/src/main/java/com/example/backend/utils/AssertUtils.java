package com.example.backend.utils;



import com.example.backend.exception.GlobalException;
import com.example.backend.utils.object.CollectionUtils;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.object.StringUtils;
import com.example.backend.utils.web.AppHttpCode;

import java.util.Collection;

public class AssertUtils {
    public static void nonNull(Object obj, String msg) {
        isTrue(obj != null, msg);
    }

    public static void nonNull(Object obj, AppHttpCode httpCode) {
        isTrue(obj != null, httpCode);
    }

    public static void nonNull(Object obj, int code, String msg) {
        isTrue(obj != null, code, msg);
    }

    public static <T> void isEquals(T o1, T o2, String msg) {
        isTrue(ObjectUtils.isEquals(o1, o2), msg);
    }

    public static <T> void isEquals(T o1, T o2, int code, String msg) {
        isTrue(ObjectUtils.isEquals(o1, o2), code, msg);
    }

    public static <T> void isEquals(T o1, T o2, AppHttpCode httpCode) {
        isTrue(ObjectUtils.isEquals(o1, o2), httpCode);
    }

    public static void nonBlank(String s, String msg) {
        isTrue(StringUtils.nonBlank(s), msg);
    }

    public static void nonBlank(String s, int code, String msg) {
        isTrue(StringUtils.nonBlank(s), code, msg);
    }

    public static void nonBlank(String s, AppHttpCode httpCode) {
        isTrue(StringUtils.nonBlank(s), httpCode);
    }

    public static void notEmpty(String str, String msg) {
        isFalse(StringUtils.isEmpty(str), msg);
    }

    public static void notEmpty(String str, int code, String msg) {
        isFalse(StringUtils.isEmpty(str), code, msg);
    }

    public static void notEmpty(String str, AppHttpCode httpCode) {
        isFalse(StringUtils.isEmpty(str), httpCode);
    }

    public static void notEmpty(Collection<?> col, String msg) {
        isTrue(CollectionUtils.notEmpty(col), msg);
    }

    public static void notEmpty(Collection<?> col, int code, String msg) {
        isTrue(CollectionUtils.notEmpty(col), code, msg);
    }

    public static void notEmpty(Collection<?> col, AppHttpCode httpCode) {
        isTrue(CollectionUtils.notEmpty(col), httpCode);
    }

    public static void isTrue(boolean ret, AppHttpCode httpCode) {
        isFalse(!ret, httpCode);
    }

    public static void isTrue(boolean ret, String msg) {
        isFalse(!ret, msg);
    }

    public static void isTrue(boolean ret, int code, String msg) {
        isFalse(!ret, code, msg);
    }

    public static void isFalse(boolean ret, String msg) {
        if(ret)
            throw new RuntimeException(msg);
    }

    public static void isFalse(boolean ret, int code, String msg) {
        if(ret)
            throw new GlobalException(code, msg);
    }

    public static void isFalse(boolean ret, AppHttpCode httpCode) {
        if(ret)
            throw new GlobalException(httpCode);
    }
}
