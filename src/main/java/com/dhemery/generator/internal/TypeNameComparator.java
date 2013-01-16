package com.dhemery.generator.internal;

import java.util.Comparator;

public class TypeNameComparator implements Comparator<String> {
    @Override
    public int compare(String left, String right) {
        return Names.simpleName(left).compareTo(Names.simpleName(right));
    }
}
