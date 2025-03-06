package com.example.backend.utils.object;


import com.example.backend.exception.GlobalException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils extends ObjectUtils {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public static final DateFormat MYSQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date defaultFormat(String dateStr) {
        try {
            if (StringUtils.isBlank(dateStr)) return null;
            if(!dateStr.contains(" ")) {
                dateStr += " 00:00:00";
            }
            return DEFAULT_DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            throw new GlobalException(400, "日期格式不正确，请输入正确的日期格式:" + DATE_PATTERN);
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

    public static Date getNextTime(long dis, TimeUnit unit) {
        return new Date(unit.toMillis(dis) + System.currentTimeMillis());
    }

    public static Long defaultFormatToLong(String dateStr) {
        return defaultFormat(dateStr).getTime();
    }

    public static String longToDefaultFormat(long dateLong) {
        return DEFAULT_DATE_FORMAT.format(new Date(dateLong));
    }

    /**
     * 将默认的格式转换为mysql的格式
     * @param dateStr 默认格式，yyyy-MM-ddTHH:mm:ssZ
     * @return mysql格式，yyyy-MM-dd HH:mm:ss
     */
    public static String defaultFormatToMysqlFormat(String dateStr) {
        String res = dateStr.replace('T', ' ');
        return res.substring(0, res.length() - 1);
    }

    public static String defaultMysqlStartTime() {
        return "1970-01-01 00:00:00";
    }

    public static String defaultMysqlEndTime() {
        return MYSQL_DATE_FORMAT.format(new Date());
    }
}
