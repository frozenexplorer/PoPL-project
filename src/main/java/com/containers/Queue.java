package com.containers;

import java.util.LinkedList;

public class Queue<T> extends ListContainer<T> {
    public Queue() {
        this.elements = new LinkedList<>();
    }

    public void enqueue(T element) {
        ((LinkedList<T>) elements).addLast(element);
    }

    public T dequeue() {
        if (isEmpty()) return null;
        return ((LinkedList<T>) elements).removeFirst();
    }
    
    public T peek() {
         if (isEmpty()) return null;
        return ((LinkedList<T>) elements).peekFirst();
    }
}
