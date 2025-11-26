package com.containers;

import java.util.*;

/**
 * Abstract base class for list-like containers.
 */
public abstract class ListContainer<T> {
    protected List<T> elements;
    protected int size;

    public ListContainer() {
        this.elements = new ArrayList<>();
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        elements.clear();
        size = 0;
    }

    /**
     * Replace element at logical index with value.
     * 
     * @return true on success; false if index out of range.
     */
    public boolean update(int index, T value) {
        if (index < 0 || index >= size)
            return false;
        elements.set(index, value);
        return true;
    }

    public boolean contains(T value) {
        return elements.contains(value);
    }

    @Override
    public String toString() {
        return String.format("%s(size=%d) %s",
                this.getClass().getSimpleName(), size, elements.toString());
    }

    // Concrete classes must implement these to define their policy
    public abstract void add(T element);

    public abstract T remove(); // remove according to policy

    public abstract T peek(); // peek according to policy

    public java.util.stream.Stream<T> stream() {
        return elements.stream();
    }
}
