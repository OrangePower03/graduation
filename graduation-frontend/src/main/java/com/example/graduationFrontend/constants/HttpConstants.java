package com.example.graduationFrontend.constants;

import okhttp3.MediaType;

public interface HttpConstants {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String HEADER_PAGE_NUM = "pageNum";

    String HEADER_PAGE_SIZE = "pageSize";

    String HEADER_AUTHENTICATION = "Authentication";
}
