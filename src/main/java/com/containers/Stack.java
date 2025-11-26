package com.containers;

import java.util.ArrayList;

public class Stack<T> extends ListContainer<T> {
    public Stack() {
        this.elements = new ArrayList<>();
    }

    public void push(T element) {
        elements.add(element);
    }

    public T pop() {
        if (isEmpty()) return null;
        return elements.remove(elements.size() - 1);
    }

    public T peek() {
        if (isEmpty()) return null;
        return elements.get(elements.size() - 1);
    }
}
