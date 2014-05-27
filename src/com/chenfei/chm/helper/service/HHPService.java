package com.chenfei.chm.helper.service;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.chenfei.chm.helper.connection.Connection;
import com.chenfei.chm.helper.listener.DefaultListener;

public class HHPService extends DefaultListener implements Connection<String> {

    /** 文件列表 */
    private LinkedList<String> hhpItems = new LinkedList<>();

    /** 排除文件表达式 */
    private Pattern            excludes;

    /** 监听是否结束 */
    private boolean            ended    = false;

    public HHPService(String excludes) {
        if (null != excludes) {
            this.excludes = Pattern.compile(excludes, Pattern.CASE_INSENSITIVE);
        }
    }

    /**
     * 根据 excludes 表达式 过滤 文件/目录
     * @param relativize 文件/目录 的 相对路径
     * @return true：过滤；false：不过滤；
     */
    private boolean isFiltered(String relativize) {
        // excludes 为空直接返回 false
        if (null == this.excludes) {
            return false;
        }
        // 返回 excludes 匹配结果
        return this.excludes.matcher(relativize).matches();
    }

    @Override
    public String get() {
        synchronized (this.hhpItems) {
            while (this.hhpItems.isEmpty()) {

                if (this.ended) {
                    return null;
                }

                try {
                    this.hhpItems.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.hhpItems.removeFirst();
        }
    }

    @Override
    public boolean isEnded() {
        return this.ended && this.hhpItems.isEmpty();
    }

    @Override
    public void file(String relativize, Path file) {

        // 排除文件
        if (this.isFiltered(relativize)) {
            return;
        }

        synchronized (this.hhpItems) {
            this.hhpItems.add(relativize);
        }
    }

    @Override
    public void end() {
        this.ended = true;
    }

}
