package com.chenfei.chm.helper.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {

    public static String getTitle(File file, String charset) {

        String title = "";

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {

            // 读取前512字节
            byte[] buffer = new byte[512];

            bufferedInputStream.read(buffer);

            // 转换为字符串
            String string = new String(buffer, charset);

            Pattern pattern = Pattern.compile("(<title>)([^<(]*)", Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(string);

            if (matcher.find()) {
                title = matcher.group(2).trim();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return title;
    }

    private HTMLUtils() {
    }
}
