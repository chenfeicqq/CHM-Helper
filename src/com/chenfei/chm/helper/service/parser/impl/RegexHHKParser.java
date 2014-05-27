package com.chenfei.chm.helper.service.parser.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chenfei.chm.helper.bean.HHKItem;
import com.chenfei.chm.helper.service.parser.HHKParser;

public class RegexHHKParser implements HHKParser {

    /** 索引文件表达式 */
    private Pattern includes;

    /** 索引文件编码 */
    private String  charset;

    /** 索引解析表达式 */
    private Regex[] regexes;

    /**
     * @param includes 索引文件表达式
     * @param charset 索引文件编码
     * @param regexes HHKItem 匹配规则
     */
    public RegexHHKParser(String includes, String charset, Regex... regexes) {
        this.includes = Pattern.compile(includes, Pattern.CASE_INSENSITIVE);
        this.charset = charset;
        this.regexes = regexes;
    }

    @Override
    public List<HHKItem> parse(String relativize, Path file) {
        // 不匹配，则结束
        if (!this.includes.matcher(relativize).matches()) {
            return null;
        }

        List<HHKItem> hhkItems = new LinkedList<>();

        try {
            Document document = Jsoup.parse(file.toFile(), this.charset);

            for (Regex regex : this.regexes) {
                hhkItems.addAll(this.parseRegex(document, regex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return hhkItems;
    }

    private List<HHKItem> parseRegex(Document document, Regex regex) {

        List<HHKItem> hhkItems = new LinkedList<>();

        Elements hhkItemElements = document.select(regex.getRegex());

        for (int index = 0, size = hhkItemElements.size(); index < size; index++) {
            Element hhkItemElement = hhkItemElements.get(index);

            String key = getFieldValue(hhkItemElement, regex.getKeyRegex());

            if (null == key) {
                continue;
            }

            String value = getFieldValue(hhkItemElement, regex.getValueRegex());

            if (null == value) {
                continue;
            }

            if (value.startsWith("../")) {
                value = value.substring(3);
            }

            hhkItems.add(new HHKItem(key, value));
        }

        return hhkItems;
    }

    private String getFieldValue(Element element, FieldRegex regex) {

        if (!regex.getValue().isEmpty()) {
            element = element.select(regex.getValue()).first();
        }

        if (null == element) {
            return null;
        }

        if (regex.isAttr()) {
            return element.attr(regex.getAttr()).trim();
        }

        if (regex.isText()) {
            return element.text().trim();
        }

        return null;
    }

    /**
     * 类RegexHHKParser.java的实现描述：HHK匹配的正则信息
     * @author chenfei.chenf 2014-4-23 下午5:40:12
     */
    public static class Regex {

        /** HHKItem 正则表达式 根节点 */
        private String     regex;

        /** HHKItem 正则表达式 Key */
        private FieldRegex keyRegex;

        /** HHKItem 正则表达式 Value */
        private FieldRegex valueRegex;

        public Regex(String regex, String keyRegex, String valueRegex) {
            this.regex = regex;
            this.keyRegex = FieldRegex.getRegex(keyRegex);
            this.valueRegex = FieldRegex.getRegex(valueRegex);
        }

        public String getRegex() {
            return regex;
        }

        public FieldRegex getKeyRegex() {
            return keyRegex;
        }

        public FieldRegex getValueRegex() {
            return valueRegex;
        }

        @Override
        public String toString() {
            return "Regex [keyRegex=" + keyRegex + ", valueRegex=" + valueRegex + "]";
        }

    }

    private static class FieldRegex {

        /** 表达式值 */
        private String value;

        /** 属性名 */
        private String attr;

        private FieldRegex(String value, String attr) {
            this.value = value;
            this.attr = attr;
        }

        public boolean isText() {
            return null == this.attr;
        }

        public boolean isAttr() {
            return null != this.attr;
        }

        public String getValue() {
            return value;
        }

        public String getAttr() {
            return attr;
        }

        public static FieldRegex getRegex(String regexStr) {
            // 文本节点 text()
            Pattern pattern = Pattern.compile("^(.*)text\\(\\)$", Pattern.CASE_INSENSITIVE);

            Matcher matcher = pattern.matcher(regexStr);

            if (matcher.find()) {
                return new FieldRegex(matcher.group(1).trim(), null);
            }

            // 属性节点 @xxx
            pattern = Pattern.compile("^(.*)@(\\S*)$", Pattern.CASE_INSENSITIVE);

            matcher = pattern.matcher(regexStr);

            if (matcher.find()) {
                return new FieldRegex(matcher.group(1).trim(), matcher.group(2));
            }

            return new FieldRegex(regexStr, null);
        }

        @Override
        public String toString() {
            return "FieldRegex [value=" + value + ", attr=" + attr + "]";
        }
    }
}
