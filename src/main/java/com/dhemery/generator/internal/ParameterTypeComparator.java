package com.dhemery.generator.internal;

import javax.lang.model.element.Element;
import java.util.Comparator;
import java.util.Objects;

public class ParameterTypeComparator implements Comparator<Element> {
    private Comparator<? super String> typeNameComparator;

    public ParameterTypeComparator(Comparator<? super String> typeNameComparator) {
        this.typeNameComparator = typeNameComparator;
    }

    @Override
    public int compare(Element left, Element right) {
        return Objects.compare(left.asType().toString(), right.asType().toString(), typeNameComparator);
    }
}
