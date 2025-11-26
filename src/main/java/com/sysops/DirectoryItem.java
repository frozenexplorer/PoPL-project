package com.sysops;

import java.util.Date;

public class DirectoryItem extends FileSystemItem {
    private int fileCount;

    public DirectoryItem(String name, long size, Date lastModified, int fileCount) {
        super(name, size, lastModified);
        this.fileCount = fileCount;
    }

    public int getFileCount() {
        return fileCount;
    }

    @Override
    public String toString() {
        return super.toString() + " [DIR] (" + fileCount + " items)";
    }
}
