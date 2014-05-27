package com.chenfei.chm.helper.parser;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.chenfei.chm.helper.bean.CHM;
import com.chenfei.chm.helper.listener.Listener;

public class Parser {

    /** 监听器 */
    private List<Listener> listeners = new ArrayList<Listener>();

    private CHM            chm;

    public Parser(CHM chm) {
        this.chm = chm;
    }

    public void parse() throws IOException {

        Path rootPath = Paths.get(chm.getRoot());

        recursiveParse(rootPath);

        // 触发结束
        triggerEnd();
    }

    private void recursiveParse(final Path rootPath) throws IOException {

        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

            private String relativize(Path path) {
                return rootPath.relativize(path).toString();
            }

            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs) throws IOException {

                String relativize = this.relativize(directory);

                if (!"".equals(relativize)) {
                    Parser.this.triggerDirectoryIn(relativize, directory);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String relativize = this.relativize(file);

                if (!"".equals(relativize) && !Parser.this.chm.isAboutFile(relativize)) {
                    Parser.this.triggerFile(relativize, file);
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path directory, IOException e) throws IOException {

                String relativize = this.relativize(directory);

                if (!"".equals(relativize)) {
                    Parser.this.triggerDirectoryOut(relativize, directory);
                }

                return FileVisitResult.CONTINUE;
            }

        });
    }

    private void triggerFile(String relativize, Path file) {
        for (Listener listener : listeners) {
            listener.file(relativize, file);
        }
    }

    private void triggerDirectoryIn(String relativize, Path directory) {
        for (Listener listener : listeners) {
            listener.directoryIn(relativize, directory);
        }
    }

    private void triggerDirectoryOut(String relativize, Path directory) {
        for (Listener listener : listeners) {
            listener.directoryOut(relativize, directory);
        }
    }

    private void triggerEnd() {
        for (Listener listener : listeners) {
            listener.end();
        }
    }

    public void addListeners(Listener listener) {
        this.listeners.add(listener);
    }
}
