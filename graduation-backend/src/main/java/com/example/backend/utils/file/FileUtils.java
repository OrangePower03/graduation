package com.example.backend.utils.file;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtils {
    public static InputStream getInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream getOutputStream(String path) {
        try {
            return new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getResourcesInputStream(String path) {
        return FileUtils.class.getClassLoader().getResourceAsStream(path);
    }

    public static List<Integer> getByPrefix(final String[] str, String prefix) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < str.length; i++) {
            if (str[i].startsWith(prefix)) {
                res.add(i);
            }
        }
        return res;
    }

    public static Map<String, Map<String, String>> getDataByPrefixAndFormat(final String[] str, String prefix) {
        List<Integer> indicators = getByPrefix(str, prefix);
        Map<String, Map<String, String>> dataMap = new HashMap<>();
        for (int indicator : indicators) {
            String key = str[indicator].substring(prefix.length());
            Map<String, String> value = new HashMap<>();
            while (++indicator < str.length && !str[indicator].isBlank()) {
                String[] split = str[indicator].split("：");
                value.put(split[0], split[1]);
            }
            dataMap.put(key, value);
        }
        System.out.println(dataMap);
        return dataMap;
    }

    public static void main(String[] args) {
        try {
            String[] str = new String(getResourcesInputStream("data.txt").readAllBytes()).split("\r\n");
            Map<String, Map<String, String>> indicatorMap = getDataByPrefixAndFormat(str, "-");
            Map<String, Map<String, String>> symptomMap = getDataByPrefixAndFormat(str, "+");
            Map<String, Map<String, String>> foodMap = getDataByPrefixAndFormat(str, "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
