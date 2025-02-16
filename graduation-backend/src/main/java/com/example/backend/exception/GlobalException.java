package com.example.backend.exception;

import com.example.backend.utils.web.AppHttpCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int code;
    private final String message;

    public GlobalException(AppHttpCode httpCode) {
        this(httpCode.getCode(), httpCode.getMessage());
    }

    public GlobalException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
