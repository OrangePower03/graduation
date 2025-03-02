package com.example.graduationFrontend.config;

import com.example.graduationFrontend.config.serial.ObjectDeserializer;
import com.example.graduationFrontend.config.serial.ObjectSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Date;
import java.util.List;


@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    private ObjectMapper objectMapper;

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

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, ObjectSerializer.DATE_INSTANCE);
        module.addDeserializer(Date.class, ObjectDeserializer.DATE_DESERIALIZER_INSTANCE);
        objectMapper.registerModule(module);
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter());
    }
}
