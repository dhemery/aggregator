package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.util.*;

import static com.dhemery.utility.aggregator.UtilityAggregator.elements;
import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

/**
 * Represents a method annotated by a utility annotation.
 */
class UtilityMethod implements Comparable<UtilityMethod> {
    final ExecutableElement methodElement;
    private final TypeWriter typeWriter = new TypeWriter();

    UtilityMethod(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }

    void write(PrintWriter out) {
        out
                .format("%n%s", comment())
                .format("    %s%s %s %s(%s)%s {%n", modifiers(), typeParameters(), returnType(), identifier(), parameters(), exceptions())
                .format("        %s%s.%s(%s);%n", statement(), className(), identifier(), arguments())
                .format("    }%n");
    }

    private String arguments() {
        List<? extends VariableElement> parameters = methodElement.getParameters();
        if (parameters.isEmpty()) return "";
        return parameters.stream()
                       .map(String::valueOf)
                       .collect(joining(", "));

    }

    private Element className() {
        return methodElement.getEnclosingElement();
    }

    private String comment() {
        return Comment.withPrefix(elements.getDocComment(methodElement), "    ");
    }

    @Override
    public int compareTo(UtilityMethod other) {
        return comparing(UtilityMethod::identifier)
                       .thenComparing(UtilityMethod::arguments)
                       .compare(this, other);
    }

    private String exceptions() {
        List<? extends TypeMirror> exceptions = methodElement.getThrownTypes();
        if (exceptions.isEmpty()) return "";
        return exceptions.stream()
                       .map(typeWriter::declare)
                       .collect(joining(", ", " throws ", ""));
    }

    private String identifier() {
        return methodElement.getSimpleName().toString();
    }

    private String modifiers() {
        return methodElement.getModifiers().stream()
                       .map(String::valueOf)
                       .collect(joining(" "));
    }

    private String parameters() {
        List<? extends VariableElement> parameters = methodElement.getParameters();
        if (parameters.isEmpty()) return "";
        return parameters.stream()
                       .map(a -> format("%s %s", a.asType(), a))
                       .collect(joining(", "));
    }

    private TypeMirror returnType() {
        return methodElement.getReturnType();
    }

    private String statement() {
        return returnType().toString().equals("void") ? "" : "return ";
    }

    private String typeParameters() {
        List<? extends TypeParameterElement> typeParameters = methodElement.getTypeParameters();
        if (typeParameters.isEmpty()) return "";
        return typeParameters.stream()
                       .map(Element::asType)
                       .map(t -> t.accept(typeWriter, new StringBuilder()))
                       .collect(joining(",", " <", ">"));
    }
}
