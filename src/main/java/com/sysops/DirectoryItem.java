package com.sysops;

import com.containers.GenericList;
import java.util.Date;

public class DirectoryItem extends FileSystemItem {
    private GenericList<FileSystemItem> children;

    public DirectoryItem(String name, long size, Date lastModified) {
        super(name, size, lastModified);
        this.children = new GenericList<>();
    }

    public void addChild(FileSystemItem item) {
        children.add(item);
    }

    public GenericList<FileSystemItem> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return super.toString() + " [DIR] (" + children.size() + " items)";
    }
}
