package com.containers;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * FIFO queue. Uses LinkedList as backing to provide O(1) enqueue/dequeue.
 */
public class Queue<T> extends ListContainer<T> {

    public Queue() {
        // replace backing with LinkedList for O(1) head/tail ops
        this.elements = new LinkedList<>();
        this.size = 0;
    }

    public void enqueue(T element) {
        Objects.requireNonNull(element);
        ((LinkedList<T>) elements).addLast(element);
        size++;
    }

    public T dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("dequeue from empty Queue");
        T val = ((LinkedList<T>) elements).removeFirst();
        size--;
        return val;
    }

    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("peek from empty Queue");
        return ((LinkedList<T>) elements).getFirst();
    }

    @Override
    public void add(T element) {
        enqueue(element);
    }

    @Override
    public T remove() {
        return dequeue();
    }
}
