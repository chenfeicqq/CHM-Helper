package com.chenfei.chm.helper.listener;

import java.nio.file.Path;

public class DefaultListener implements Listener {

    @Override
    public void file(String relativize, Path file) {
    }

    @Override
    public void directoryIn(String relativize, Path directory) {
    }

    @Override
    public void directoryOut(String relativize, Path directory) {
    }

    @Override
    public void end() {
    }

}
