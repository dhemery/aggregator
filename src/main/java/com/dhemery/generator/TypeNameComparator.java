package com.dhemery.generator;

import java.util.Comparator;

import static com.dhemery.generator.Names.simpleName;

public class TypeNameComparator implements Comparator<String> {
    @Override
    public int compare(String left, String right) {
        return simpleName(left).compareTo(simpleName(right));
    }
}
