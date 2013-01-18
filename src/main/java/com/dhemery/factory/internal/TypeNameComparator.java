package com.dhemery.factory.internal;

import java.util.Comparator;

/**
 * Compares type names by alphabatically comparing their simple names (i.e. their names with any package names removed).
 */
public class TypeNameComparator implements Comparator<String> {
    @Override
    public int compare(String left, String right) {
        return Names.simpleName(left).compareTo(Names.simpleName(right));
    }
}
