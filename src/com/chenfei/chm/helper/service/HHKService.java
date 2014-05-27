package com.chenfei.chm.helper.service;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import com.chenfei.chm.helper.bean.HHKItem;
import com.chenfei.chm.helper.connection.Connection;
import com.chenfei.chm.helper.listener.DefaultListener;
import com.chenfei.chm.helper.service.parser.HHKParser;

public class HHKService extends DefaultListener implements Connection<HHKItem> {

    /** 键值对列表 */
    private LinkedList<HHKItem> hhkItems = new LinkedList<>();

    /** 索引解析器 */
    private HHKParser           hhkParser;

    /** 监听是否结束 */
    private boolean             ended    = false;

    public HHKService(HHKParser hhkParser) {
        this.hhkParser = hhkParser;
    }

    @Override
    public HHKItem get() {
        synchronized (this.hhkItems) {
            while (this.hhkItems.isEmpty()) {

                if (this.ended) {
                    return null;
                }

                try {
                    this.hhkItems.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.hhkItems.removeFirst();
        }
    }

    @Override
    public boolean isEnded() {
        return this.ended && this.hhkItems.isEmpty();
    }

    @Override
    public void file(String relativize, Path file) {
        synchronized (this.hhkItems) {
            List<HHKItem> hhkItem = this.hhkParser.parse(relativize, file);

            if (null != hhkItem) {
                this.hhkItems.addAll(hhkItem);
            }
        }
    }

    @Override
    public void end() {
        this.ended = true;
    }

}
