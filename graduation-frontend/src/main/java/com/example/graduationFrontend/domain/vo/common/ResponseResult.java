package com.example.graduationFrontend.domain.vo.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.graduationFrontend.constants.HttpConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> extends ResponseBody implements Serializable {
    private int code;

    private String msg;

    private T data;

    public ResponseResult(JSONObject json, Class<T> bodyClass) {
        code = json.getInteger("code");
        msg = json.getString("msg");
        data = JSON.parseObject(json.getString("data"), bodyClass);
    }

    public boolean isSuccess() {
        return code == 200;
    }

    @Override
    public MediaType contentType() {
        return HttpConstants.JSON;
    }

    @Override
    public long contentLength() {
        System.out.println("contentLength");
        return 0;
    }

    @Override
    public BufferedSource source() {
        System.out.println("source");
        return null;
    }
}
