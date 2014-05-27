package com.chenfei.chm.helper.creater;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.CountDownLatch;

import com.chenfei.chm.helper.bean.CHM;
import com.chenfei.chm.helper.bean.HHKItem;
import com.chenfei.chm.helper.connection.Connection;

/**
 * 类HHKCreater.java的实现描述：HHK 文件的创建线程
 * @author chenfei.chenf 2014-5-4 上午10:42:26
 */
public class HHKCreater implements Runnable {

    private CHM                 chm;

    private Connection<HHKItem> hhkItems;

    private CountDownLatch      countDownLatch;

    public HHKCreater(CHM chm, Connection<HHKItem> hhkItems, CountDownLatch countDownLatch) {
        this.chm = chm;
        this.hhkItems = hhkItems;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.chm.getHHKPath()), "GB2312"))) {

            this.writeHead(bufferedWriter);

            while (!this.hhkItems.isEnded()) {

                HHKItem hhkItem = this.hhkItems.get();

                if (null == hhkItem) {
                    break;
                }

                this.writeItem(bufferedWriter, hhkItem);
            }

            this.writeFoot(bufferedWriter);

        } catch (Throwable e) {
            e.printStackTrace();
        }

        this.countDownLatch.countDown();
    }

    private void writeHead(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML//EN\">");
        bufferedWriter.newLine();
        bufferedWriter.write("<HTML><HEAD></HEAD><BODY><UL>");
        bufferedWriter.newLine();
    }

    private void writeItem(BufferedWriter bufferedWriter, HHKItem hhkItem) throws IOException {

        bufferedWriter.write("<LI><OBJECT type=\"text/sitemap\">");
        bufferedWriter.newLine();

        bufferedWriter.write("<param name=\"Name\" value=\"" + hhkItem.getKey() + "\">");
        bufferedWriter.newLine();

        bufferedWriter.write("<param name=\"Local\" value=\"" + hhkItem.getValue() + "\">");
        bufferedWriter.newLine();

        bufferedWriter.write("</OBJECT></LI>");
        bufferedWriter.newLine();
    }

    private void writeFoot(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("</UL></BODY></HTML>");
    }
}
