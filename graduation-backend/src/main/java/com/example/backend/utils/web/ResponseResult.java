package com.example.backend.utils.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 封装为响应体的类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private static final ResponseResult EMPTY_DATA_SUCCESS_RESPONSE = new ResponseResult();

    private int code;

    private String msg;

    private T data;

    private ResponseResult() {
        this(null);
    }

    private ResponseResult(T data) {
        this(data, AppHttpCode.SUCCESS);
    }

    private ResponseResult(T data, AppHttpCode httpCode){
        this.code = httpCode.getCode();
        this.msg = httpCode.getMessage();
        this.data = data;
    }

    /**
     * 错误的请求
     * @return 请求体
     */
    public static <T> ResponseResult<T> error() {
        return error(AppHttpCode.SYSTEM_ERROR);
    }

    public static <T> ResponseResult<T> error(int code, String msg) {
        return error(new AppHttpCode(code, msg));
    }

    public static <T> ResponseResult<T> error(AppHttpCode httpCode) {
        return error(null, httpCode);
    }

    public static <T> ResponseResult<T> error(T data, AppHttpCode httpCode) {
        return new ResponseResult<>(data, httpCode);
    }


    /**
     * 响应成功的请求
     * @return 响应体
     */
    public static <T> ResponseResult<T> ok() {
        return EMPTY_DATA_SUCCESS_RESPONSE;
    }

    public static <T> ResponseResult<T> ok(int code, String msg) {
        return ok(new AppHttpCode(code, msg));
    }

    public static <T> ResponseResult<T> ok(AppHttpCode httpCode) {
        return ok(null, httpCode);
    }

    public static <T> ResponseResult<T> ok(@Nullable T data, AppHttpCode httpCode) {
        return new ResponseResult<>(data, httpCode);
    }

    public static <T> ResponseResult<T> ok(@NonNull T data) {
        return new ResponseResult<>(data);
    }
}
