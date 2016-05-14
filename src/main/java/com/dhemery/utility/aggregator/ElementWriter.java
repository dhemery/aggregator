package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;


class ElementWriter extends SimpleElementVisitor8<StringBuilder, StringBuilder> {
    private TypeVisitor<StringBuilder, StringBuilder> typeWriter;

    ElementWriter(TypeVisitor<StringBuilder, StringBuilder> typeWriter) {
        this.typeWriter = typeWriter;
    }

    String declare(Element element) {
        return element.accept(this, new StringBuilder()).toString();
    }

    @Override
    public StringBuilder visitExecutable(ExecutableElement e, StringBuilder declaration) {
        declareModifiers(e.getModifiers(), declaration);
        declareTypeParameters(e.getTypeParameters(), declaration);
        declaration.append(format("%s ", e.getReturnType()));
        return declaration;
    }

    private void declareTypeParameters(List<? extends TypeParameterElement> typeParameters, StringBuilder declaration) {
        if (typeParameters.isEmpty()) return;
        declaration.append(
                typeParameters.stream()
                        .map(this::declare)
                        .collect(joining(", ", "<", "> "))
        );
    }

    private void declareModifiers(Set<Modifier> modifiers, StringBuilder declaration) {
        modifiers.stream()
                .forEach(m -> declaration.append(format("%s ", m)));
    }

    @Override
    public StringBuilder visitTypeParameter(TypeParameterElement e, StringBuilder declaration) {
        return e.asType().accept(typeWriter, declaration);
    }
}
