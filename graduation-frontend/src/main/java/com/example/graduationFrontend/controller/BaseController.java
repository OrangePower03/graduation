package com.example.graduationFrontend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.graduationFrontend.constants.HttpConstants;
import com.example.graduationFrontend.constants.HttpMethod;
import com.example.graduationFrontend.domain.dto.BaseDTO;
import com.example.graduationFrontend.domain.vo.BaseVO;
import com.example.graduationFrontend.domain.vo.common.ResponseResult;
import lombok.NonNull;
import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class BaseController {
    public static final String BASE_URL = "http://localhost:8080";
    private final OkHttpClient client = new OkHttpClient();
    public static final RequestBody EMPTY_REQUEST_BODY = new BaseDTO();

    public Request buildGetRequest(@NonNull String url,
                                   @NonNull Map<String, String> headers,
                                   Map<String, String> param) {

        url = buildUrl(url, param);
        Request.Builder requestBuilder = new Request.Builder()
                .headers(Headers.of(headers))
                .url(BASE_URL + url);
        return requestBuilder.build();
    }

    public Request buildGetRequest(@NonNull String url,
                                   String authentication,
                                   Map<String, String> param) {

        return buildGetRequest(url, Map.of(HttpConstants.HEADER_AUTHENTICATION, authentication), param);
    }

    public Request buildRequest(@NonNull String url,
                                @NonNull Map<String, String> headers,
                                Map<String, String> param,
                                HttpMethod method,
                                BaseDTO body) {

        url = buildUrl(url, param);
        Request.Builder requestBuilder = new Request.Builder()
                .method(method.name(), body == null ? EMPTY_REQUEST_BODY : body)
                .headers(Headers.of(headers))
                .url(BASE_URL + url);
        return requestBuilder.build();
    }

    public Request buildRequest(@NonNull String url,
                                String token,
                                Map<String, String> param,
                                HttpMethod method,
                                BaseDTO body) {

        return buildRequest(url, Map.of(HttpConstants.HEADER_AUTHENTICATION, token), param, method, body);
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

    public <T> ResponseResult<T> sendRequest(Request request, Class<T> bodyClass) {
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String bodyStr = response.body().string();
            JSONObject middleBody = JSON.parseObject(bodyStr);
            return new ResponseResult<>(middleBody, bodyClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> ResponseResult<List<T>> sendRequestAsList(Request request, Class<T> bodyClass) {
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String bodyStr = response.body().string();
            JSONObject middleBody = JSON.parseObject(bodyStr);
            int code = middleBody.getInteger("code");
            String msg = middleBody.getString("msg");
            List<T> data = middleBody.getJSONArray("data").toJavaList(bodyClass);
            return new ResponseResult<>(code, msg, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public <T> ResponseResult<List<T>> sendRequest(Request request, Class<T> bodyClass) {
//        try (Response response = client.newCall(request).execute()) {
//            assert response.body() != null;
//            String bodyStr = response.body().string();
//            JSONObject middleBody = JSON.parseObject(bodyStr);
//            return new ResponseResult<>(middleBody, bodyClass);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public ResponseResult<Void> sendRequest(Request request) {
        return sendRequest(request, Void.class);
    }

    public String error(Model model, String msg) {
        model.addAttribute("errorMessage", msg);
        return "/error";
    }
}
