package com.example.graduationFrontend.constants;

import lombok.Getter;

@Getter
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");
    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }
}
