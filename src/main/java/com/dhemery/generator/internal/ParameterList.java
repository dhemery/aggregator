package com.dhemery.generator.internal;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParameterList implements Comparable<ParameterList> {
    public final List<String> typeNames;

    public ParameterList(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    public ParameterList(Iterable<? extends Element> parameters) {
        this(simpleTypeNames(parameters));
    }

    @Override
    public int compareTo(ParameterList that) {
        return Objects.compare(this.typeNames, that.typeNames, new TypeNameListComparator());
    }

    @Override
    public String toString() {
        return typeNames.toString();
    }

    private static List<String> simpleTypeNames(Iterable<? extends Element> parameters) {
        List<String> names = new ArrayList<>();
        for(Element parameter : parameters) {
            names.add(nameOf(parameter));
        }
        return names;
    }

    private static String nameOf(Element parameter) {
        String qualifiedName = parameter.asType().toString();
        if(!qualifiedName.contains(".")) return qualifiedName;
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }
}
