package com.dhemery.generator.internal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AParameterTypeList {
    public final List<String> leftNames = new ArrayList<>();
    public final List<String> rightNames = new ArrayList<>();
    public final ParameterList left = new ParameterList(leftNames);
    public final ParameterList right = new ParameterList(rightNames);

    @Test
    public void emptyListsAreEqual() {
        assertThat(left, comparesEqualTo(right));
        assertThat(right, comparesEqualTo(left));
    }

    @Test
    public void identicalListsAreEqual() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        leftNames.addAll(names);
        rightNames.addAll(names);
        assertThat(left, comparesEqualTo(right));
        assertThat(right, comparesEqualTo(left));
    }

    @Test
    public void ifOneListIsAPrefixOfTheOther_theLongerListIsGreater() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        leftNames.addAll(names);
        rightNames.addAll(names);
        rightNames.add("AdditionalName");
        assertThat(left, lessThan(right));
        assertThat(right, greaterThan(left));
    }

    @Test
    public void ifCorrespondingItemsDiffer_theListWithTheGreaterItemIsGreater() {
        List<String> names = Arrays.asList("Foo", "Bar", "com.dhemery.foo.bar.baz.Baz");
        leftNames.addAll(names);
        rightNames.addAll(names);
        leftNames.add("Pluribus");
        rightNames.add("Unum");
        assertThat(left, lessThan(right));
        assertThat(right, greaterThan(left));
    }
}
