package com.dhemery.utility.aggregator;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;

import static java.util.stream.Collectors.joining;


public class ElementWriter extends SimpleElementVisitor8<List<String>,List<String>> {
    private TypeVisitor<List<String>, List<String>> typeWriter;

    public ElementWriter(TypeVisitor<List<String>, List<String>> typeWriter) {
        this.typeWriter = typeWriter;
    }

    @Override
    public List<String> visitTypeParameter(TypeParameterElement e, List<String> strings) {
        strings.add(e.toString());
        strings.add(bounds(e));
        return strings;
    }

    private String bounds(TypeParameterElement e) {
        return e.getBounds().stream()
                       .flatMap(b -> b.accept(typeWriter, new ArrayList<>()).stream())
                       .filter(name -> !Objects.equals(name, "java.lang.Object"))
                       .collect(Joining.orEmpty(",", " extends ", ""));
    }
}
