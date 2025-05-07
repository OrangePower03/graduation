package com.example.backend.service;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.backend.constants.UserConstants;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

@Service
public class ChatModelService {
    private final Application application = new Application();

    public static ApplicationParam buildApplicationParam(String indicatorName, Integer indicatorState) {
//        String message = "患者的测试的指标：%s数据出现异常，数据较于标准值趋向于较%s".formatted(indicatorName, indicatorState);
        JsonObject params = new JsonObject();
        params.addProperty("indicatorName", indicatorName);
        params.addProperty("indicatorState", UserConstants.USER_INDICATOR_HIGH.equals(indicatorState) ? "高" : "低");
        return ApplicationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .appId("ebe0800b73f9440897dd7b6a950e3956")
                .bizParams(params)
                .prompt("请返回输出")
                .build();
    }

    public String generate(String indicatorName, Integer indicatorState) {
        try {
            ApplicationResult result = application.call(buildApplicationParam(indicatorName, indicatorState));
            return result.getOutput().getText();
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }

    }
}
