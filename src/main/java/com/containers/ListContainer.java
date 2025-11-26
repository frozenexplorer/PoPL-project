package com.containers;

import java.util.List;

public abstract class ListContainer<T> {
    protected List<T> elements;
    protected int size;

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    public String toString() {
        return elements.toString();
    }
}
