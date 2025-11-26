package com.sysops;

import java.util.Date;

public class FileItem extends FileSystemItem {
    private String extension;

    public FileItem(String name, long size, Date lastModified, String extension) {
        super(name, size, lastModified);
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return super.toString() + " [FILE]";
    }
}
