package com.example.graduationFrontend.constants;

import okhttp3.MediaType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface HttpConstants {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String HEADER_PAGE_NUM = "pageNum";

    String HEADER_PAGE_SIZE = "pageSize";

    String HEADER_AUTHENTICATION = "Authentication";

    String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
}
