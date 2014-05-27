package com.chenfei.chm.helper.listener;

import java.nio.file.Path;

public interface Listener {

    /**
     * 文件
     * 
     * @param relativize 文件相对路径
     * @param file 文件路径
     */
    void file(String relativize, Path file);

    /**
     * 进入文件夹
     * 
     * @param relativize 文件夹相对路径
     * @param directory 文件夹路径
     */
    void directoryIn(String relativize, Path directory);

    /**
     * 退出文件夹
     * 
     * @param relativize 文件夹相对路径
     * @param directory 文件夹路径
     */
    void directoryOut(String relativize, Path directory);

    /**
     * 监听结束
     */
    void end();
}
