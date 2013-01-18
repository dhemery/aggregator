package com.dhemery.factory.internal;

import org.junit.Test;

import java.util.Comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ATypeNameComparator {
    Comparator<String> comparator = new TypeNameComparator();

    @Test
    public void sortsAlphabetically() {
        assertThat(comparator.compare("Foo", "Bar"), is(greaterThan(0)));
    }

    @Test
    public void ignoresPackageNames() {
        assertThat(comparator.compare("a.Foo", "b.Bar"), is(greaterThan(0)));
    }
}
