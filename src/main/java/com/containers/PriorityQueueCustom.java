package com.containers;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;

public class PriorityQueueCustom<T> extends ListContainer<T> {
    private PriorityQueue<T> pq;

    public PriorityQueueCustom(Comparator<T> comparator) {
        this.pq = new PriorityQueue<>(comparator);
        this.elements = new ArrayList<>(); // Dummy backing
    }

    public void add(T element) {
        pq.add(element);
        elements.add(element); // Keep sync for size()
    }

    public T remove() {
        T el = pq.poll();
        if(el != null) elements.remove(el);
        return el;
    }
    
    public T peek() {
        return pq.peek();
    }
}
