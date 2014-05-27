package com.chenfei.chm.helper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.chenfei.chm.helper.bean.CHM;
import com.chenfei.chm.helper.creater.CHMCreater;
import com.chenfei.chm.helper.creater.HHCCreater;
import com.chenfei.chm.helper.creater.HHKCreater;
import com.chenfei.chm.helper.creater.HHPCreater;
import com.chenfei.chm.helper.parser.Parser;
import com.chenfei.chm.helper.service.HHCService;
import com.chenfei.chm.helper.service.HHKService;
import com.chenfei.chm.helper.service.HHPService;
import com.chenfei.chm.helper.service.parser.impl.RegexHHKParser;
import com.chenfei.chm.helper.service.parser.impl.RegexHHKParser.Regex;

public class Main {

    public static void main(String[] args) throws IOException {
        // 文件名中不要出现空格
        CHM chm = new CHM("c:/Users/chenfei.chenf/Desktop/api", "JDK-1.8", "JDK 1.8 API by chenfei", "index.html");

        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(new CHMCreater(chm, countDownLatch)).start();

        Parser parser = new Parser(chm);

        HHPService hhpService = new HHPService(null);
        HHPCreater hhpCreater = new HHPCreater(chm, hhpService, countDownLatch);
        new Thread(hhpCreater).start();
        parser.addListeners(hhpService);

        HHCService hhcService = new HHCService("(index-files|resources|script|stylesheet).*");
        HHCCreater hhcCreater = new HHCCreater(chm, hhcService, countDownLatch);
        new Thread(hhcCreater).start();
        parser.addListeners(hhcService);

        HHKService hhkService = new HHKService(new RegexHHKParser("index-files\\\\.*", "GB2312", new Regex("dl>dt>a", "span text()", "@href")));
        HHKCreater hhkCreater = new HHKCreater(chm, hhkService, countDownLatch);
        new Thread(hhkCreater).start();
        parser.addListeners(hhkService);

        parser.parse();
    }
}
