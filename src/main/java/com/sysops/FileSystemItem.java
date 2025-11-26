package com.sysops;

import java.util.Date;

public abstract class FileSystemItem {
    protected String name;
    protected long size;
    protected Date lastModified;

    public FileSystemItem(String name, long size, Date lastModified) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return String.format("%-20s %10d bytes  %s", name, size, lastModified);
    }
}
