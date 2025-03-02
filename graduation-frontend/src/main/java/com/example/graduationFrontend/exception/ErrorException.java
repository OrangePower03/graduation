package com.example.graduationFrontend.exception;

import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import lombok.Data;

@Data
public class ErrorException extends RuntimeException {
    private String msg;

    public ErrorException(ResponseResult responseResult) {
        this(responseResult.getMsg());
    }

    public ErrorException(String errorMsg) {
        super(errorMsg);
        this.msg = errorMsg;
    }
}
