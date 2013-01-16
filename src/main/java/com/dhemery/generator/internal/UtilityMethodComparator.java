package com.dhemery.generator.internal;

import javax.lang.model.element.Element;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class UtilityMethodComparator implements Comparator<UtilityMethod> {
    private final Comparator<? super List<? extends Element>> parameterListComparator;

    public UtilityMethodComparator(Comparator<? super List<? extends Element>> parameterListComparator) {
        this.parameterListComparator = parameterListComparator;
    }

    @Override
    public int compare(UtilityMethod left, UtilityMethod right) {
        int methodNameComparison = left.name().compareTo(right.name());
        if(methodNameComparison != 0) return methodNameComparison;
        List<? extends Element> leftParameters = left.parameters();
        List<? extends Element> rightParameters = right.parameters();
        return Objects.compare(leftParameters, rightParameters, parameterListComparator);
    }
}
