package com.example.backend.annotation;


import com.example.backend.aop.VerifyRequestData;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示启用请求体校验的注解
 * ！！！启用的方法第一个参数必须是请求体，也就是BaseDTO的子类！！！
 * 默认会校验参数中所有的字段是否存在 null 的，如果某些字段允许为 null，通过注解{@link FieldNullable} 标识
 * 但要注意这里不会校验字符串为空的情况，需要另外校验才行
 * 详细代码请看 {@link VerifyRequestData}
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyRequestBody {
}
