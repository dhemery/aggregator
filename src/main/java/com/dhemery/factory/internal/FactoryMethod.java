package com.dhemery.factory.internal;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

/**
 * Represents a factory method annotated by a factory annotation.
 */
public class FactoryMethod {
    private final String declaringClass;
    private final String javadocComment;
    private final Set<Modifier> modifiers;
    private final List<? extends TypeParameterElement> typeParameters;
    private final String returnType;
    private final String name;
    private final List<? extends VariableElement> parameters;
    private final List<? extends TypeMirror> thrownTypes;

    public FactoryMethod(ExecutableElement methodElement, ProcessingEnvironment environment) {
        declaringClass = methodElement.getEnclosingElement().toString();
        modifiers = methodElement.getModifiers();
        typeParameters = methodElement.getTypeParameters();
        returnType = methodElement.getReturnType().toString();
        name = methodElement.getSimpleName().toString();
        parameters = methodElement.getParameters();
        thrownTypes = methodElement.getThrownTypes();
        javadocComment = environment.getElementUtils().getDocComment(methodElement);
    }

    public String declaringClass() {
        return declaringClass;
    }

    public String javadocComment() {
        return javadocComment;
    }

    public Iterable<Modifier> modifiers() {
        return modifiers;
    }

    public String name() {
        return name;
    }

    public List<? extends Element> parameters() {
        return parameters;
    }

    public String returnType() {
        return returnType;
    }

    public List<? extends TypeMirror> thrownTypes() {
        return thrownTypes;
    }

    public Iterable<? extends TypeParameterElement> typeParameters() {
        return typeParameters;
    }
}
