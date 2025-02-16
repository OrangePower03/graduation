package com.example.backend.utils.object;

public class StringUtils extends ObjectUtils {
    public static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        return isNull(str) || str.isEmpty();
    }

    public static boolean nonEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return isNull(str) || str.isBlank();
    }

    public static boolean nonBlank(String str) {
        return !isBlank(str);
    }

    public static String getNonNull(String str) {
        return isNull(str) ? EMPTY : str;
    }

    public static boolean isMatch(String str, String regex) {
        return nonNull(str) && str.matches(regex);
    }
}
