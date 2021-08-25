package com.emerson.bpm.util;

import java.util.Iterator;
import java.util.Arrays; /* For sample code */

public class IteratorIterator<T> implements Iterator<T> {
    private final Iterator<T> is[];
    private int current;

    public IteratorIterator(Iterator<T>... iterators)
    {
            is = iterators;
            current = 0;
    }

    public boolean hasNext() {
            while ( current < is.length && !is[current].hasNext() )
                    current++;

            return current < is.length;
    }

    public T next() {
            while ( current < is.length && !is[current].hasNext() )
                    current++;

            return is[current].next();
    }

    public void remove() { /* not implemented */ }

    /* Sample use */
    public static void main(String... args)
    {
            Iterator<Integer> a = Arrays.asList(1,2,3,4).iterator();
            Iterator<Integer> b = Arrays.asList(10,11,12).iterator();
            Iterator<Integer> c = Arrays.asList(99, 98, 97).iterator();

            Iterator<Integer> ii = new IteratorIterator<Integer>(a,b,c);

            while ( ii.hasNext() )
                    System.out.println(ii.next());
    }
}