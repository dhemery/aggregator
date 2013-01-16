package com.dhemery.generator.internal;

import java.util.Comparator;
import java.util.List;

class TypeNameListComparator implements Comparator<List<String>> {
    @Override
    public int compare(List<String> these, List<String> those) {
        for(int i = 0 ; i < these.size() ; i++) {
            if(those.size() <= i) break;
            int comparison = these.get(i).compareTo(those.get(i));
            if(comparison != 0) return comparison;
        }
        return these.size() - those.size();
    }
}
