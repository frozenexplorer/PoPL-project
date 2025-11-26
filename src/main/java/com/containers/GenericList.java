package com.containers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.stream.Stream;

public class GenericList<T> extends ListContainer<T> {
    public GenericList() {
        this.elements = new ArrayList<>();
    }

    public void add(T element) {
        elements.add(element);
    }

    public T get(int index) {
        return elements.get(index);
    }
    
    public void removeAt(int index) {
        elements.remove(index);
    }

    public Stream<T> stream() {
        return elements.stream();
    }
    
    public void sort(Comparator<? super T> c) {
        elements.sort(c);
    }
}
