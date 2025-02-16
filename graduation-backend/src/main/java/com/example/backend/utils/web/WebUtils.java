package com.example.backend.utils.web;

import com.alibaba.fastjson.JSON;
import com.example.backend.utils.object.ObjectUtils;
import com.example.backend.utils.object.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils {
    private final static  Logger log = LoggerFactory.getLogger(WebUtils.class);

    private WebUtils() {}

    /**
     * 获取请求的HttpServletRequest对象，请不要在过滤器中调用该方法
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ObjectUtils.requireNonNull(requestAttributes).getRequest();
    }

    /**
     * 获取请求的HttpServletResponse对象，请不要在过滤器中调用该方法
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ObjectUtils.requireNonNull(requestAttributes).getResponse();
    }

    /**
     * 获取用户ip地址，请不要在过滤器中调用该方法
     * https://test.always-will.seelove-tech.com:8088/ip
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        return getIp(request);
    }

    /**
     * 这个方法可以在过滤器中调用，传入过滤器中的请求
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP"); // 需要和nginx配置呼应
        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (ip != null && ip.contains(",")) {
                ip = ip.split(",")[0];  // 获取第一个 IP
            }
        }
        return StringUtils.isBlank(ip) ? request.getRemoteAddr() : ip;
    }

    /**
     * 获取用户端口，请不要在过滤器中调用该方法
     */
    public static int getPort() {
        HttpServletRequest request = getRequest();
        return getPort(request);
    }

    /**
     * 这个方法可以在过滤器重调用，传入过滤器中的请求
     */
    public static int getPort(HttpServletRequest request) {
        String port = request.getHeader("X-Real-Port"); // 需要和nginx配置呼应
        return StringUtils.isBlank(port) ? request.getRemotePort() : Integer.parseInt(port);
    }


    /**
     * 渲染响应，用于过滤器
     */
    public static void render(HttpServletRequest request, HttpServletResponse response, AppHttpCode httpCode) {
        render(request, response, httpCode.getCode(), httpCode.getMessage());
    }

    public static void render(HttpServletRequest request, HttpServletResponse response, int code, String msg) {
        logError(request, code, msg);
        response.setStatus(400); // 使用自定义的状态码会导致postman无法接收到响应
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().print(JSON.toJSONString(ResponseResult.error(code, msg)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 打印错误日志
     */
    public static void logError(HttpServletRequest request, int code, String msg) {
        String ip = getIp(request);
        int port = getPort(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.error("请求ip为[{}:{}]的用户发出请求地址为 '{}' 发生异常，异常状态码为<{}>，异常信息为：{}",
            ip, port, method + " " + uri, code, msg);
    }

}
