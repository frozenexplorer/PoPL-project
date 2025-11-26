package com.containers;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * LIFO stack implemented on top of ListContainer.
 */
public class Stack<T> extends ListContainer<T> {

    public Stack() {
        super(); // array-backed is fine for stack
    }

    public void push(T element) {
        Objects.requireNonNull(element);
        elements.add(element);
        size++;
    }

    public T pop() {
        if (isEmpty())
            throw new NoSuchElementException("pop from empty Stack");
        T val = elements.remove(size - 1);
        size--;
        return val;
    }

    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("peek from empty Stack");
        return elements.get(size - 1);
    }

    // ListContainer API mappings
    @Override
    public void add(T element) {
        push(element);
    }

    @Override
    public T remove() {
        return pop();
    }
}
