package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

/**
 * Represents a method annotated by a utility annotation.
 */
class UtilityMethod implements Comparable<UtilityMethod> {
    private final String comment;
    private final List<? extends TypeParameterElement> typeParameters;
    private final Set<Modifier> modifiers;
    private final TypeMirror result;
    private final String declaringClass;
    private final String identifier;
    private final List<? extends VariableElement> parameters;
    private final List<? extends TypeMirror> thrownTypes;
    private final TypeWriter typeWriter = new TypeWriter();
    private final ElementWriter elementWriter = new ElementWriter(typeWriter);

    UtilityMethod(ExecutableElement methodElement) {
        declaringClass = methodElement.getEnclosingElement().toString();
        modifiers = methodElement.getModifiers();
        typeParameters = methodElement.getTypeParameters();
        result = methodElement.getReturnType();
        identifier = methodElement.getSimpleName().toString();
        parameters = methodElement.getParameters();
        thrownTypes = methodElement.getThrownTypes();
        comment = Comment.forMethod(UtilityAggregator.elements.getDocComment(methodElement));
    }

    void write(PrintWriter out) {
        out.format("%n%s", Describe.the(result));
        out.format("%s", comment)
                .format("%n    %s %s%s %s(%s) %s{",
                        modifier(),
                        typeParameters(),
                        result,
                        identifier,
                        formalParameterList(),
                        exceptions())
                .format("%n        return %s.%s(%s);",
                        declaringClass,
                        identifier,
                        argumentList())
        .format("%n    }%n");
    }

    private String argumentList() {
        return parameters.stream()
                       .map(String::valueOf)
                       .collect(joining(", "));
    }

    private String exceptions() {
        return thrownTypes.stream()
                       .map(typeWriter::declare)
                       .collect(Joining.orEmpty(", ", "throws ", " "));
    }

    private String formalParameterList() {
        return parameters.stream()
                       .map(p -> format("%s %s", p.asType(), p.getSimpleName()))
                       .collect(joining(", "));
    }


    private String modifier() {
        return modifiers.stream()
                       .map(String::valueOf)
                       .collect(joining(" "));
    }

    private String simpleName() {
        return identifier;
    }

    private String typeParameters() {
        return typeParameters.stream()
                       .map(elementWriter::declare)
                       .collect(Joining.orEmpty(", ", "<", "> "));
    }

    @Override
    public int compareTo(UtilityMethod other) {
        return comparing(UtilityMethod::simpleName)
                       .thenComparing(UtilityMethod::parameterTypeNames)
                       .compare(this, other);
    }

    private String parameterTypeNames() {
        return parameters.stream()
                       .map(Element::getSimpleName)
                       .collect(joining("#"));
    }
}
