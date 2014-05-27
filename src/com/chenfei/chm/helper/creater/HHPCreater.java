package com.chenfei.chm.helper.creater;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.CountDownLatch;

import com.chenfei.chm.helper.bean.CHM;
import com.chenfei.chm.helper.connection.Connection;

/**
 * 类HHPCreater.java的实现描述：HHP 文件的创建线程
 * @author chenfei.chenf 2014-5-4 上午10:42:36
 */
public class HHPCreater implements Runnable {

    private CHM                chm;

    private Connection<String> hhpItems;

    private CountDownLatch     countDownLatch;

    public HHPCreater(CHM chm, Connection<String> hhpItems, CountDownLatch countDownLatch) {
        this.chm = chm;
        this.hhpItems = hhpItems;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.chm.getHHPPath()), "GB2312"))) {

            this.writeHead(bufferedWriter);

            while (!this.hhpItems.isEnded()) {

                String hhcItem = this.hhpItems.get();

                if (null == hhcItem) {
                    break;
                }

                this.writeItem(bufferedWriter, hhcItem);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.countDownLatch.countDown();
    }

    private void writeHead(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("[OPTIONS]");
        bufferedWriter.newLine();
        bufferedWriter.write("Compatibility=1.1 or later");
        bufferedWriter.newLine();
        bufferedWriter.write("Language=0x804 中文(中国)");
        bufferedWriter.newLine();
        bufferedWriter.write("Compiled file=" + this.chm.getName());
        bufferedWriter.newLine();
        bufferedWriter.write("Default topic=" + this.chm.getIndex());
        bufferedWriter.newLine();
        bufferedWriter.write("Title=" + this.chm.getTitle());
        bufferedWriter.newLine();
        bufferedWriter.write("Contents file=" + this.chm.getHHCName());
        bufferedWriter.newLine();
        bufferedWriter.write("Index file=" + this.chm.getHHKName());
        bufferedWriter.newLine();
        bufferedWriter.write("[FILES]");
        bufferedWriter.newLine();
    }

    private void writeItem(BufferedWriter bufferedWriter, String hhcItem) throws IOException {
        bufferedWriter.write(hhcItem);
        bufferedWriter.newLine();
    }
}
