package com.dhemery.utility.aggregator;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;


class ElementWriter extends SimpleElementVisitor8<List<String>,List<String>> {
    private TypeVisitor<List<String>, List<String>> typeWriter;

    ElementWriter(TypeVisitor<List<String>, List<String>> typeWriter) {
        this.typeWriter = typeWriter;
    }

    @Override
    public List<String> visitTypeParameter(TypeParameterElement e, List<String> strings) {
        return e.asType().accept(typeWriter, strings);
    }
}
