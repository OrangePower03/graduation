package com.example.graduationFrontend.controller;

import com.alibaba.fastjson.JSON;
import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.BaseDTO;
import com.example.graduationFrontend.domain.vo.BaseVO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import lombok.NonNull;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public abstract class BaseController {
    public static final String BASE_URL = "http://localhost:8080";
    private final OkHttpClient client = new OkHttpClient();

    public Request buildGetRequest(@NonNull String url,
                                   Map<String, String> headers,
                                   Map<String, String> param) {

        url = buildUrl(url, param);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .headers(Headers.of(headers))
                .build();
        return request;
    }

    public Request buildRequest(@NonNull String url,
                                Map<String, String> headers,
                                Map<String, String> param,
                                HttpMethod method,
                                BaseDTO body) {

        url = buildUrl(url, param);
        Request request = new Request.Builder()
                .method(method.name(), body)
                .url(BASE_URL + url)
                .headers(Headers.of(headers))
                .build();
        return request;
    }

    private String buildUrl(String url, Map<String, String> param) {
        if (!url.startsWith("/")) url = "/" + url;
        if (param != null && !param.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.substring(0, urlBuilder.length() - 1);
        }
        return url;
    }

    public <T> ResponseResult<T> sendRequest(Request request, Class<T> bodyClass) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            String bodyStr = response.body().string();
            ResponseResult result = JSON.parseObject(bodyStr, ResponseResult.class);
            return result;

        } finally {

        }
    }

}
