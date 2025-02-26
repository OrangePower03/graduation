package com.example.graduationFrontend.domain.vo.common;

import com.example.graduationFrontend.constants.HttpConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> extends ResponseBody implements Serializable {
    private int code;

    private String msg;

    private T data;

    @Override
    public MediaType contentType() {
        return HttpConstants.JSON;
    }

    @Override
    public long contentLength() {
        System.out.println("contentLength");
        return 0;
    }

    @Override
    public BufferedSource source() {
        System.out.println("source");
        return null;
    }
}
