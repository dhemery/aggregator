package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class MethodWriter implements TypeVisitor<StringBuilder, StringBuilder>, ElementVisitor<StringBuilder, StringBuilder> {
    @Override
    public StringBuilder visit(Element e) {
        return visit(e, new StringBuilder());
    }

    @Override
    public StringBuilder visit(Element e, StringBuilder declaration) {
        return e.accept(this, declaration);
    }

    @Override
    public StringBuilder visit(TypeMirror t) {
        return visit(t, new StringBuilder());
    }

    @Override
    public StringBuilder visit(TypeMirror t, StringBuilder declaration) {
        return t.accept(this, declaration);
    }

    @Override
    public StringBuilder visitArray(ArrayType t, StringBuilder declaration) {
        return declaration.append(format("/* visitArray %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitDeclared(DeclaredType t, StringBuilder declaration) {
        return declaration.append(t.asElement());
    }

    @Override
    public StringBuilder visitError(ErrorType t, StringBuilder declaration) {
        return declaration.append(format("/* visitError %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitExecutable(ExecutableElement e, StringBuilder declaration) {
        declaration.append(format("%n    "));

        declaration.append(format("%s %s", modifiersOf(e), typeParametersOf(e)));

        declaration.append(format("Void %s() {%n", e.getSimpleName()));
        declaration.append(format("        return null;%n"));
        declaration.append(format("    }%n"));

        return declaration;
    }

    @Override
    public StringBuilder visitExecutable(ExecutableType t, StringBuilder declaration) {
        return declaration.append(format("/* visitExecutable %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitIntersection(IntersectionType t, StringBuilder declaration) {
        return declaration.append(format("/* visitIntersection %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitNoType(NoType t, StringBuilder declaration) {
        return declaration.append(format("/* visitNoType %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitNull(NullType t, StringBuilder declaration) {
        return declaration.append(format("/* visitNull %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitPackage(PackageElement e, StringBuilder declaration) {
        return declaration.append(format("/* visitPackage %s (%s) */", e, e.getClass()));
    }

    @Override
    public StringBuilder visitPrimitive(PrimitiveType t, StringBuilder declaration) {
        return declaration.append(format("/* visitPrimitive %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitType(TypeElement e, StringBuilder declaration) {
        return declaration.append(format("/* visitType %s (%s) */", e, e.getClass()));
    }

    @Override
    public StringBuilder visitTypeParameter(TypeParameterElement e, StringBuilder declaration) {
        return declaration
                       .append(format("/* type parameter name */%s", e.getSimpleName()))
                       .append(boundsOf(e));
    }

    @Override
    public StringBuilder visitTypeVariable(TypeVariable t, StringBuilder declaration) {
        return declaration
                       .append(t)
                       .append(bound(t.getUpperBound(), "extends"));
    }

    @Override
    public StringBuilder visitUnion(UnionType t, StringBuilder declaration) {
        return declaration.append(format("/* visitUnion %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitUnknown(Element e, StringBuilder declaration) {
        return declaration.append(format("/* visitUnknown (element) %s (%s) */", e, e.getClass()));
    }

    @Override
    public StringBuilder visitUnknown(TypeMirror t, StringBuilder declaration) {
        return declaration.append(format("/* visitUnknown (type) %s (%s) */", t, t.getClass()));
    }

    @Override
    public StringBuilder visitVariable(VariableElement e, StringBuilder declaration) {
        return declaration.append(format("/* visitVariable %s (%s) */", e, e.getClass()));
    }

    @Override
    public StringBuilder visitWildcard(WildcardType t, StringBuilder declaration) {
        return declaration.append(format("/* visitWildcard %s (%s) */", t, t.getClass()));
    }

    private String bound(TypeMirror bound, String prefix) {
        return Optional.of(bound)
                       .map(this::visit)
                       .map(b -> format(" %s %s", prefix, b))
                       .orElse("");
    }

    private String boundsOf(TypeParameterElement e) {
        List<? extends TypeMirror> bounds = e.getBounds();
        if (bounds.isEmpty()) return "";
        return bounds.stream()
                       .map(this::visit)
                       .collect(joining(",", " boundsOf ", ""));
    }

    private String modifiersOf(ExecutableElement method) {
        return method.getModifiers().stream()
                       .map(String::valueOf)
                       .collect(joining(" "));
    }

    private String typeParametersOf(Parameterizable element) {
        List<? extends TypeParameterElement> typeParameters = element.getTypeParameters();
        if (typeParameters.isEmpty()) return "";
        return typeParameters.stream()
                       .map(Element::asType)
                       .map(this::visit)
                       .collect(joining(",", "<", "> "));
    }
}
