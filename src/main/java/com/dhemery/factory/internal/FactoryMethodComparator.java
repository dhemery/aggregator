package com.dhemery.factory.internal;

import javax.lang.model.element.Element;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Compares factory methods first by comparing their names,
 * and then by applying the given comparator to their parameter lists.
 */
public class FactoryMethodComparator implements Comparator<FactoryMethod> {
    private final Comparator<? super List<? extends Element>> parameterListComparator;

    public FactoryMethodComparator(Comparator<? super List<? extends Element>> parameterListComparator) {
        this.parameterListComparator = parameterListComparator;
    }

    @Override
    public int compare(FactoryMethod left, FactoryMethod right) {
        int methodNameComparison = left.name().compareTo(right.name());
        if(methodNameComparison != 0) return methodNameComparison;
        List<? extends Element> leftParameters = left.parameters();
        List<? extends Element> rightParameters = right.parameters();
        return Objects.compare(leftParameters, rightParameters, parameterListComparator);
    }
}
