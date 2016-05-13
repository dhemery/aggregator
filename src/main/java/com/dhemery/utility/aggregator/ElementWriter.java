package com.dhemery.utility.aggregator;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;


public class ElementWriter extends SimpleElementVisitor8<List<String>,List<String>> {
    private TypeVisitor<List<String>, List<String>> typeWriter;

    public ElementWriter(TypeVisitor<List<String>, List<String>> typeWriter) {
        this.typeWriter = typeWriter;
    }

    @Override
    public List<String> visitTypeParameter(TypeParameterElement e, List<String> strings) {
        strings.add(e.toString());
        List<String> bounds = new ArrayList<>();
        e.getBounds().stream().forEach(b -> b.accept(typeWriter, bounds));
        strings.add(bounds.stream().collect(joining(",", " extends ", "")));
        return strings;
    }
}
