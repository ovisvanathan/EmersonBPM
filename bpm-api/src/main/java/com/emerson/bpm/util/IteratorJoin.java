package com.emerson.bpm.util;

import java.util.Iterator;

public class IteratorJoin<T> implements Iterator<T> {
    private final Iterator<T> first, next;

    public IteratorJoin(Iterator<T> first, Iterator<T> next) {
        this.first = first;
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return first.hasNext() || next.hasNext();
    }

    @Override
    public T next() {
        if (first.hasNext())
            return first.next();
        return next.next();
    }
}