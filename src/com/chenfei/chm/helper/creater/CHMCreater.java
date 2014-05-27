package com.chenfei.chm.helper.creater;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import com.chenfei.chm.helper.bean.CHM;

/**
 * 类CHMCreater.java的实现描述：创建 CHM 文件的线程
 * @author chenfei.chenf 2014-5-4 上午10:41:47
 */
public class CHMCreater implements Runnable {

    private CHM            chm;

    private CountDownLatch countDownLatch;

    public CHMCreater(CHM chm, CountDownLatch countDownLatch) {
        this.chm = chm;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            this.countDownLatch.await();

            this.create();

            Files.delete(Paths.get(this.chm.getHHPPath()));
            Files.delete(Paths.get(this.chm.getHHCPath()));
            Files.delete(Paths.get(this.chm.getHHKPath()));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void create() throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec(this.getCommand());

        try (InputStream inputStream = process.getInputStream()) {
            while (true) {
                if (inputStream.read() == -1) {
                    break;
                }
            }
        }
    }

    private String getCommand() {
        StringBuilder commandStringBuilder = new StringBuilder();
        commandStringBuilder.append("ext\\hhc.exe \"");
        commandStringBuilder.append(this.chm.getHHPPath().replace("/", "\\"));
        commandStringBuilder.append("\"");
        return commandStringBuilder.toString();
    }
}
