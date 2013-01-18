package com.dhemery.factory.internal;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Compares lists by applying the given comparator to correspoding elements of the lists.
 * @param <T> the type of object in the lists.
 */
public class ListComparator<T> implements Comparator<List<? extends T>> {
    private final Comparator<T> elementComparator;

    public ListComparator(Comparator<T> elementComparator) {
        this.elementComparator = elementComparator;
    }

    @Override
    public int compare(List<? extends T> left, List<? extends T> right) {
        for(int i = 0 ; i < left.size() ; i++) {
            if(right.size() <= i) break;
            int comparison = Objects.compare(left.get(i), right.get(i), elementComparator);
            if(comparison != 0) return comparison;
        }
        return left.size() - right.size();
    }
}
