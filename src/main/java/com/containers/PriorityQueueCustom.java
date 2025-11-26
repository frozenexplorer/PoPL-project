package com.containers;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Binary-heap backed priority queue. By default constructs a min-heap using
 * natural ordering
 * if T implements Comparable<T>. A Comparator can be passed to control
 * ordering.
 *
 * Note: This class intentionally named PriorityQueueCustom to avoid clashing
 * with java.util.PriorityQueue.
 */
public class PriorityQueueCustom<T> extends ListContainer<T> {
    private final Comparator<? super T> comparator; // null means natural Comparable
    // We'll use elements as the heap array

    public PriorityQueueCustom() {
        this(null);
    }

    public PriorityQueueCustom(Comparator<? super T> comparator) {
        super();
        this.comparator = comparator;
    }

    private int compare(T a, T b) {
        if (comparator != null)
            return comparator.compare(a, b);
        // natural ordering
        @SuppressWarnings("unchecked")
        Comparable<? super T> ca = (Comparable<? super T>) a;
        return ca.compareTo(b);
    }

    @Override
    public void add(T element) {
        Objects.requireNonNull(element);
        elements.add(element);
        size++;
        bubbleUp(size - 1);
    }

    @Override
    public T remove() {
        if (isEmpty())
            throw new NoSuchElementException("remove from empty PriorityQueue");
        // root is element 0
        T root = elements.get(0);
        T last = elements.remove(size - 1);
        size--;
        if (!isEmpty()) {
            elements.set(0, last);
            bubbleDown(0);
        }
        return root;
    }

    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("peek from empty PriorityQueue");
        return elements.get(0);
    }

    private void bubbleUp(int index) {
        int i = index;
        while (i > 0) {
            int parent = (i - 1) / 2;
            T cur = elements.get(i);
            T par = elements.get(parent);
            if (compare(cur, par) < 0) { // cur has higher priority (min-heap)
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int i = index;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && compare(elements.get(left), elements.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && compare(elements.get(right), elements.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest != i) {
                swap(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        T tmp = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, tmp);
    }
}
