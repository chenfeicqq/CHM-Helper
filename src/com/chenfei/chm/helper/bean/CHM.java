package com.chenfei.chm.helper.bean;

/**
 * 类CHM.java的实现描述：CHM 文件相关信息
 * @author chenfei.chenf 2014-5-4 上午10:41:08
 */
public class CHM {

    /** CHM文件标题 */
    private String title;

    /** CHM文件首页 */
    private String index;

    /** 根目录 */
    private String root;

    /** CHM文件名 */
    private String name;

    /** HHP文件名 */
    private String hhpName;

    /** HHC文件名 */
    private String hhcName;

    /** HHK文件名 */
    private String hhkName;

    /**
     * @param root 根目录
     * @param name CHM文件名
     * @param title 标题
     * @param index CHM文件首页
     */
    public CHM(String root, String name, String title, String index) {
        this.title = title;
        this.index = index;
        this.root = root;
        this.name = name + ".chm";
        this.hhpName = name + ".hhp";
        this.hhcName = name + ".hhc";
        this.hhkName = name + ".hhk";
    }

    public boolean isAboutFile(String path) {
        return path.endsWith(".chm") || path.endsWith(".hhp") || path.endsWith(".hhc") || path.endsWith(".hhk");
    }

    public String getTitle() {
        return this.title;
    }

    public String getIndex() {
        return this.index;
    }

    public String getRoot() {
        return root;
    }

    public String getName() {
        return this.name;
    }

    public String getHHPName() {
        return this.hhpName;
    }

    public String getHHCName() {
        return this.hhcName;
    }

    public String getHHKName() {
        return this.hhkName;
    }

    public String getHHPPath() {
        return this.root + "/" + this.hhpName;
    }

    public String getHHCPath() {
        return this.root + "/" + this.hhcName;
    }

    public String getHHKPath() {
        return this.root + "/" + this.hhkName;
    }

    @Override
    public String toString() {
        return "CHM [title=" + title + ", index=" + index + ", root=" + root + ", name=" + name + ", hhpName=" + hhpName + ", hhcName=" + hhcName
               + ", hhkName=" + hhkName + "]";
    }

}
