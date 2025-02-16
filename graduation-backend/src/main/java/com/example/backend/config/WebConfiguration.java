package com.example.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Date;
import java.util.List;


@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")   // 允许跨域的路径
                .allowedOriginPatterns("*")     // 允许跨域请求的域名
                .allowCredentials(true)         // 是否允许cookie
                .allowedMethods("GET", "POST", "DELETE", "PUT")  //允许的请求方法
                .allowedHeaders("*")
                .maxAge(3600);                  // 允许跨域时间
    }
}
