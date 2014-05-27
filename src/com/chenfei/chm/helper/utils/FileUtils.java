package com.chenfei.chm.helper.utils;

public class FileUtils {

    /**
     * 分割文件名
     * 
     * @param fileName 文件名
     * @return 
     */
    public static String[] split(String fileName) {
        String[] fileNameInfos = new String[2];

        int splitPoint = fileName.lastIndexOf(".");

        if (-1 == splitPoint) {
            fileNameInfos[0] = fileName;
            fileNameInfos[1] = null;
        } else {
            fileNameInfos[0] = fileName.substring(0, splitPoint);
            fileNameInfos[1] = fileName.substring(splitPoint);
        }

        return fileNameInfos;
    }

    private FileUtils() {
    }
}
