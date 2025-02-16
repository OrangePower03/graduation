package com.example.backend.exception.handler;

import com.example.backend.constants.HttpStatus;
import com.example.backend.exception.GlobalException;
import com.example.backend.utils.web.ResponseResult;
import com.example.backend.utils.web.WebUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获，暂时先不考虑太多异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseResult<?> handleGlobalException(GlobalException e, HttpServletRequest request) {
        WebUtils.logError(request, e.getCode(), e.getMessage());
        return ResponseResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e, HttpServletRequest request) {
        WebUtils.logError(request, HttpStatus.SYSTEM_ERROR, e.getMessage());
        return ResponseResult.error(HttpStatus.SYSTEM_ERROR, e.getMessage());
    }
}
