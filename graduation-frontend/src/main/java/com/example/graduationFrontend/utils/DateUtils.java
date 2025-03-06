package com.example.graduationFrontend.utils;


import com.example.graduationFrontend.exception.ErrorException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    public static Date defaultFormat(String dateStr) {
        try {
            return DEFAULT_DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new ErrorException("日期格式不正确，请输入正确的日期格式:" + DATE_PATTERN);
        }
    }


    public static String defaultFormat(Date date) {
        return DEFAULT_DATE_FORMAT.format(date);
    }

    public static Date format(String dateStr, String pattern) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
