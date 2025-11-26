package com.containers;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Deque<T> extends ListContainer<T> {
    public Deque() {
        this.elements = new LinkedList<>();
    }

    public void addFirst(T element) {
        ((LinkedList<T>) elements).addFirst(element);
    }

    public void addLast(T element) {
        ((LinkedList<T>) elements).addLast(element);
    }

    public T removeFirst() {
        if (isEmpty())
            return null;
        return ((LinkedList<T>) elements).removeFirst();
    }

    public T removeLast() {
        if (isEmpty())
            return null;
        return ((LinkedList<T>) elements).removeLast();
    }

    public Stream<T> stream() {
        return elements.stream();
    }
}
