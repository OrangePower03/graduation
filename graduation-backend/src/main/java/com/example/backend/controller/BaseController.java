package com.example.backend.controller;

import com.example.backend.utils.web.ResponseResult;
import lombok.NonNull;

public abstract class BaseController {
    protected ResponseResult<Void> ok() {
        return ResponseResult.ok();
    }

    protected <T> ResponseResult<T> ok(@NonNull T data) {
        return ResponseResult.ok(data);
    }
}
