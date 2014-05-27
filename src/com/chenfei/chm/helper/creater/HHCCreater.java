package com.chenfei.chm.helper.creater;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.CountDownLatch;

import com.chenfei.chm.helper.bean.CHM;
import com.chenfei.chm.helper.bean.HHCItem;
import com.chenfei.chm.helper.connection.Connection;
import com.chenfei.chm.helper.utils.FileUtils;
import com.chenfei.chm.helper.utils.HTMLUtils;

/**
 * 类HHCCreater.java的实现描述：HHC 文件的创建线程
 * @author chenfei.chenf 2014-5-4 上午10:42:11
 */
public class HHCCreater implements Runnable {

    private CHM                 chm;

    private Connection<HHCItem> hhcItems;

    private CountDownLatch      countDownLatch;

    public HHCCreater(CHM chm, Connection<HHCItem> hhcItems, CountDownLatch countDownLatch) {
        this.chm = chm;
        this.hhcItems = hhcItems;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.chm.getHHCPath()), "GB2312"))) {

            this.writeHead(bufferedWriter);

            while (!this.hhcItems.isEnded()) {

                HHCItem hhcItem = this.hhcItems.get();

                if (null == hhcItem) {
                    break;
                }

                if (hhcItem.isDirectory()) {
                    if (hhcItem.isIn()) {
                        this.writeDirectoryIn(bufferedWriter, hhcItem);
                    }
                    if (hhcItem.isOut()) {
                        this.writeDirectoryOut(bufferedWriter, hhcItem);
                    }
                } else {
                    this.writeFile(bufferedWriter, hhcItem);
                }
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
        bufferedWriter.write("<HTML><HEAD></HEAD><BODY>");
        bufferedWriter.newLine();
        bufferedWriter.write("<OBJECT type=\"text/site properties\"><param name=\"Window Styles\" value=\"0x800025\"/></OBJECT>");
        bufferedWriter.newLine();
        bufferedWriter.write("<UL>");
        bufferedWriter.newLine();
    }

    private void writeFile(BufferedWriter bufferedWriter, HHCItem hhcItem) throws IOException {
        String title = "";

        String[] fileNameInfos = FileUtils.split(hhcItem.getFile().getName());
        String extension = fileNameInfos[1];

        if (null != extension && (".html".equalsIgnoreCase(extension) || ".htm".equalsIgnoreCase(extension))) {
            title = HTMLUtils.getTitle(hhcItem.getFile(), "GB2312");
        }

        if (title.isEmpty()) {
            title = fileNameInfos[0];
        }

        if (!title.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<LI><OBJECT type=\"text/sitemap\"><param name=\"Name\" value=\"");
            stringBuilder.append(title);
            stringBuilder.append("\"/><param name=\"Local\" value=\"");
            stringBuilder.append(hhcItem.getRelativize());
            stringBuilder.append("\"/></OBJECT></LI>");

            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.newLine();
        }
    }

    private void writeDirectoryIn(BufferedWriter bufferedWriter, HHCItem hhcItem) throws IOException {
        bufferedWriter.write("<LI><OBJECT type=\"text/sitemap\"><param name=\"Name\" value=\"" + hhcItem.getFile().getName() + "\"/></OBJECT></LI>");
        bufferedWriter.newLine();
        bufferedWriter.write("<UL>");
        bufferedWriter.newLine();
    }

    private void writeDirectoryOut(BufferedWriter bufferedWriter, HHCItem hhcItem) throws IOException {
        bufferedWriter.write("</UL>");
        bufferedWriter.newLine();
    }

    private void writeFoot(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("</UL>");
        bufferedWriter.newLine();
        bufferedWriter.write("</BODY></HTML>");
    }
}
