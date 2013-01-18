package com.dhemery.factory.internal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AListComparator {
    private final Comparator<String> stringComparator = stringComparator();
    private final Comparator<? super List<? extends String>> stringListComparator = new ListComparator<>(stringComparator);
    private final List<String> left = new ArrayList<>();
    private final List<String> right = new ArrayList<>();

    @Test
    public void emptyListsAreEqual() {
        assertThat(stringListComparator.compare(left, right), is(0));
        assertThat(stringListComparator.compare(right, left), is(0));
    }

    @Test
    public void identicalListsAreEqual() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        left.addAll(names);
        right.addAll(names);
        assertThat(stringListComparator.compare(left, right), is(0));
        assertThat(stringListComparator.compare(right, left), is(0));
    }

    @Test
    public void ifOneListIsAPrefixOfTheOther_theLongerListIsGreater() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        left.addAll(names);
        right.addAll(names);
        right.add("AdditionalName");
        assertThat(stringListComparator.compare(left, right), is(lessThan(0)));
        assertThat(stringListComparator.compare(right, left), is(greaterThan(0)));
    }

    @Test
    public void ifCorrespondingItemsDiffer_theListWithTheGreaterItemIsGreater() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        left.addAll(names);
        right.addAll(names);
        left.add("Pluribus");
        right.add("Unum");
        assertThat(stringListComparator.compare(left, right), is(lessThan(0)));
        assertThat(stringListComparator.compare(right, left), is(greaterThan(0)));
    }

    private Comparator<String> stringComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String left, String right) {
                return left.compareTo(right);
            }
        };
    }
}
