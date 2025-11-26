package com.containers;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Double-ended queue with O(1) operations at both ends.
 */
public class Deque<T> extends ListContainer<T> {

    public Deque() {
        this.elements = new LinkedList<>();
        this.size = 0;
    }

    public void addFirst(T element) {
        Objects.requireNonNull(element);
        ((LinkedList<T>) elements).addFirst(element);
        size++;
    }

    public void addLast(T element) {
        Objects.requireNonNull(element);
        ((LinkedList<T>) elements).addLast(element);
        size++;
    }

    public T removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("removeFirst from empty Deque");
        T val = ((LinkedList<T>) elements).removeFirst();
        size--;
        return val;
    }

    public T removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("removeLast from empty Deque");
        T val = ((LinkedList<T>) elements).removeLast();
        size--;
        return val;
    }

    public T peekFirst() {
        if (isEmpty())
            throw new NoSuchElementException("peekFirst from empty Deque");
        return ((LinkedList<T>) elements).getFirst();
    }

    public T peekLast() {
        if (isEmpty())
            throw new NoSuchElementException("peekLast from empty Deque");
        return ((LinkedList<T>) elements).getLast();
    }

    // default add/remove/peek map to back/front semantics:
    @Override
    public void add(T element) {
        addLast(element);
    }

    @Override
    public T remove() {
        return removeFirst();
    }

    @Override
    public T peek() {
        return peekFirst();
    }
}
