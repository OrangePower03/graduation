package com.example.backend.utils.file;

import java.io.*;
import java.util.Arrays;
import java.util.List;
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

    public static List<String> getByPrefix(InputStream stream, String prefix) {
        try {
            String s = new String(stream.readAllBytes());
            return Arrays.stream(s.split("\n"))
                    .filter(str -> str.strip().startsWith(prefix))
                    .map(str -> str.strip().substring(prefix.length()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        getByPrefix(FileUtils.getInputStream("C:\\Users\\Administrator\\Desktop\\graduation\\身体指标.md"), "/*/").forEach(System.out::println);
    }
}
