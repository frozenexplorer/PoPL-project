package com.containers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Full-featured list with index-based access, stable merge sort, and common
 * list operations.
 * Array-backed (ArrayList).
 */
public class GenericList<T> extends ListContainer<T> {

    public GenericList() {
        super(); // elements is an ArrayList by default
    }

    public void addFirst(T element) {
        Objects.requireNonNull(element);
        elements.add(0, element);
        size++;
    }

    public void addLast(T element) {
        add(element); // append
    }

    @Override
    public void add(T element) {
        Objects.requireNonNull(element);
        elements.add(element);
        size++;
    }

    public void addAt(int index, T element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("addAt index out of range");
        Objects.requireNonNull(element);
        elements.add(index, element);
        size++;
    }

    public T removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("removeFirst from empty GenericList");
        T val = elements.remove(0);
        size--;
        return val;
    }

    public T removeLast() {
        return remove();
    }

    @Override
    public T remove() {
        if (isEmpty())
            throw new NoSuchElementException("remove from empty GenericList");
        T val = elements.remove(size - 1);
        size--;
        return val;
    }

    public T removeAt(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("removeAt index out of range");
        T val = elements.remove(index);
        size--;
        return val;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("get index out of range");
        return elements.get(index);
    }

    @Override
    public T peek() {
        if (isEmpty())
            throw new NoSuchElementException("peek from empty GenericList");
        return elements.get(size - 1);
    }

    /**
     * Stable merge sort. If comparator is null, T must implement Comparable.
     */
    public void sort(Comparator<? super T> comparator) {
        if (size <= 1)
            return;
        List<T> tmp = new ArrayList<>(size);
        // ensure tmp size
        for (int i = 0; i < size; i++)
            tmp.add(null);
        mergeSort(0, size - 1, comparator, tmp);
    }

    private void mergeSort(int l, int r, Comparator<? super T> comp, List<T> tmp) {
        if (l >= r)
            return;
        int m = (l + r) >>> 1;
        mergeSort(l, m, comp, tmp);
        mergeSort(m + 1, r, comp, tmp);
        merge(l, m, r, comp, tmp);
    }

    private void merge(int l, int m, int r, Comparator<? super T> comp, List<T> tmp) {
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            T a = elements.get(i), b = elements.get(j);
            int cmp;
            if (comp != null)
                cmp = comp.compare(a, b);
            else {
                @SuppressWarnings("unchecked")
                Comparable<? super T> ca = (Comparable<? super T>) a;
                cmp = ca.compareTo(b);
            }
            if (cmp <= 0) { // stable: left element preferred when equal
                tmp.set(k++, a);
                i++;
            } else {
                tmp.set(k++, b);
                j++;
            }
        }
        while (i <= m)
            tmp.set(k++, elements.get(i++));
        while (j <= r)
            tmp.set(k++, elements.get(j++));
        for (int idx = l; idx <= r; idx++)
            elements.set(idx, tmp.get(idx));
    }
}
