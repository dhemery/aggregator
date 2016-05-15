package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class MethodWriter implements TypeVisitor<Void, Consumer<String>>, ElementVisitor<Void, Consumer<String>> {
    private static final Consumer<String> COMPLAIN = (s) -> {
        throw new RuntimeException(format("Unexpected consumption of %s", s));
    };

    @Override
    public Void visit(Element e) {
        return visit(e, COMPLAIN);
    }

    @Override
    public Void visit(Element e, Consumer<String> declaration) {
        return e.accept(this, declaration);
    }

    @Override
    public Void visit(TypeMirror t) {
        return visit(t, COMPLAIN);
    }

    @Override
    public Void visit(TypeMirror t, Consumer<String> declaration) {
        return t.accept(this, declaration);
    }

    @Override
    public Void visitArray(ArrayType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitArray %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Consumer<String> declaration) {
        declaration.accept(String.valueOf(t.asElement()));
        return null;
    }

    @Override
    public Void visitError(ErrorType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitError %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableElement e, Consumer<String> declaration) {
        declaration.accept(format("%n    "));

        declaration.accept(format("%s %s", modifiersOf(e), typeParametersOf(e)));

        declaration.accept(format("Void %s() {%n", e.getSimpleName()));
        declaration.accept(format("        return null;%n"));
        declaration.accept(format("    }%n"));
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitExecutable %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitIntersection(IntersectionType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitIntersection %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitNoType(NoType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitNoType %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitNull(NullType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitNull %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitPackage(PackageElement e, Consumer<String> declaration) {
        declaration.accept(format("/* visitPackage %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitPrimitive %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitType(TypeElement e, Consumer<String> declaration) {
        declaration.accept(format("/* visitType %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitTypeParameter(TypeParameterElement e, Consumer<String> declaration) {
        declaration.accept(format("/* type parameter name */%s", e.getSimpleName()));
        declaration.accept(boundsOf(e));
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Consumer<String> declaration) {
        declaration.accept(String.valueOf(t));
        declaration.accept(bound(t.getUpperBound(), "extends"));
        return null;
    }

    @Override
    public Void visitUnion(UnionType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitUnion %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitUnknown(Element e, Consumer<String> declaration) {
        declaration.accept(format("/* visitUnknown (element) %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitUnknown(TypeMirror t, Consumer<String> declaration) {
        declaration.accept(format("/* visitUnknown (type) %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Consumer<String> declaration) {
        declaration.accept(format("/* visitVariable %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Consumer<String> declaration) {
        declaration.accept(format("/* visitWildcard %s (%s) */", t, t.getClass()));
        return null;
    }

    private String bound(TypeMirror bound, String prefix) {
        return Optional.of(bound)
                       .map(this::visit)
                       .map(b -> format(" %s %s", prefix, b))
                       .orElse("");
    }

    private String boundsOf(TypeParameterElement e) {
        StringJoiner joiner = new StringJoiner(",", " boundsOf ", "").setEmptyValue("");
        List<? extends TypeMirror> bounds = e.getBounds();
        bounds.stream()
                .forEach(b -> b.accept(this, joiner::add));
        return joiner.toString();
    }

    private String modifiersOf(ExecutableElement method) {
        return method.getModifiers().stream()
                       .map(String::valueOf)
                       .collect(joining(" "));
    }

    private String typeParametersOf(Parameterizable element) {
        StringJoiner joiner = new StringJoiner(",", "<", "> ").setEmptyValue("");
        element.getTypeParameters().stream()
                .map(Element::asType)
                .map(String::valueOf)
                .forEach(joiner::add);
        return joiner.toString();
    }
}
