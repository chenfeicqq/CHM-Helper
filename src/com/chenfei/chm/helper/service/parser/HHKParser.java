package com.chenfei.chm.helper.service.parser;

import java.nio.file.Path;
import java.util.List;

import com.chenfei.chm.helper.bean.HHKItem;

public interface HHKParser {

    /**
     * HHK解析器
     * 
     * @param relativize 文件相对路径
     * @param file 索引文件
     * @return
     */
    List<HHKItem> parse(String relativize, Path file);
}
