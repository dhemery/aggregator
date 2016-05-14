package com.dhemery.utility.aggregator;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.*;


class ElementWriter extends SimpleElementVisitor8<StringBuilder,StringBuilder> {
    private TypeVisitor<StringBuilder, StringBuilder> typeWriter;

    ElementWriter(TypeVisitor<StringBuilder, StringBuilder> typeWriter) {
        this.typeWriter = typeWriter;
    }

    String declare(Element element) {
        return element.accept(this, new StringBuilder()).toString();
    }

    @Override
    public StringBuilder visitTypeParameter(TypeParameterElement e, StringBuilder declaration) {
        return e.asType().accept(typeWriter, declaration);
    }
}
