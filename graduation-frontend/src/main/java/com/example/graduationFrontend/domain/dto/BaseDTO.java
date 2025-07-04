package com.example.graduationFrontend.domain.dto;

import com.alibaba.fastjson.JSON;
import com.example.graduationFrontend.constants.HttpConstants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class BaseDTO extends RequestBody implements Serializable {
    @Serial
    private static final long serialVersionUID = 114514L;

    @Override
    public MediaType contentType() {
        return HttpConstants.JSON;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(JSON.toJSONString(this).getBytes());
    }
}
