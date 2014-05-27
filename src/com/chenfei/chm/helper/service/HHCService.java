package com.chenfei.chm.helper.service;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.chenfei.chm.helper.bean.HHCItem;
import com.chenfei.chm.helper.bean.HHCItem.Action;
import com.chenfei.chm.helper.connection.Connection;
import com.chenfei.chm.helper.listener.DefaultListener;

public class HHCService extends DefaultListener implements Connection<HHCItem> {

    /** 文件列表 */
    private LinkedList<HHCItem> hhcItems = new LinkedList<>();

    /** 排除文件表达式 */
    private Pattern             excludes;

    /** 监听是否结束 */
    private boolean             ended    = false;

    public HHCService(String excludes) {
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
    public HHCItem get() {
        synchronized (this.hhcItems) {
            while (this.hhcItems.isEmpty()) {

                if (this.ended) {
                    return null;
                }

                try {
                    this.hhcItems.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.hhcItems.removeFirst();
        }
    }

    @Override
    public boolean isEnded() {
        return this.ended && this.hhcItems.isEmpty();
    }

    @Override
    public void file(String relativize, Path file) {

        // 排除文件
        if (this.isFiltered(relativize)) {
            return;
        }

        synchronized (this.hhcItems) {
            this.hhcItems.add(new HHCItem(relativize, file, null));
        }
    }

    @Override
    public void directoryIn(String relativize, Path directory) {

        // 排除路径
        if (this.isFiltered(relativize)) {
            return;
        }

        synchronized (this.hhcItems) {
            this.hhcItems.add(new HHCItem(relativize, directory, Action.IN));
        }
    }

    @Override
    public void directoryOut(String relativize, Path directory) {

        // 排除路径
        if (this.isFiltered(relativize)) {
            return;
        }

        synchronized (this.hhcItems) {
            this.hhcItems.add(new HHCItem(relativize, directory, Action.OUT));
        }
    }

    @Override
    public void end() {
        this.ended = true;
    }

}
