package com.chenfei.chm.helper.bean;

import java.io.File;
import java.nio.file.Path;

public class HHCItem {

    private String relativize;

    private Path   path;

    private Action action;

    public HHCItem(String relativize, Path path, Action action) {
        this.relativize = relativize;
        this.path = path;
        this.action = action;
    }

    public boolean isDirectory() {
        return null != this.action;
    }

    public boolean isIn() {
        return Action.IN == this.action;
    }

    public boolean isOut() {
        return Action.OUT == this.action;
    }

    public String getRelativize() {
        return relativize;
    }

    public File getFile() {
        return path.toFile();
    }

    @Override
    public String toString() {
        return "HHCItem [relativize=" + relativize + ", path=" + path + ", action=" + action + "]";
    }

    public enum Action {
        /** 进入 */
        IN,
        /** 退出 */
        OUT;
    }
}
