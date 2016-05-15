package com.dhemery.utility.aggregator;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.dhemery.utility.aggregator.UtilityAggregator.elements;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class MethodWriter implements TypeVisitor<Void, Consumer<String>>, ElementVisitor<Void, Consumer<String>> {
    private static final Consumer<String> COMPLAIN = (s) -> {
        throw new RuntimeException(format("Unexpected consumption of %s", s));
    };
    private final TypeMapper typeMapper;

    MethodWriter(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public Void visit(Element e) {
        return visit(e, COMPLAIN);
    }

    @Override
    public Void visit(Element e, Consumer<String> action) {
        return e.accept(this, action);
    }

    @Override
    public Void visit(TypeMirror t) {
        return visit(t, COMPLAIN);
    }

    @Override
    public Void visit(TypeMirror t, Consumer<String> action) {
        return t.accept(this, action);
    }

    @Override
    public Void visitArray(ArrayType t, Consumer<String> action) {
        action.accept(format("/* visitArray %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Consumer<String> action) {
        action.accept(format("/*DeclaredType*/%s%s", typeMapper.name(t), typeParametersOf(t)));
        return null;
    }

    @Override
    public Void visitError(ErrorType t, Consumer<String> action) {
        action.accept(format("/* visitError %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableElement method, Consumer<String> action) {
        String declaration = new StringBuilder()
                                     .append(format("%n%s", commentFor(method)))
                                     .append(format("    %s %s%s %s(%s) %s{%n", modifiersOf(method), typeParametersOf(method), returnTypeOf(method), nameOf(method), parametersOf(method), exceptionsThrownBy(method)))
                                     .append(format("        %s%s.%s(%s);%n", statementFor(method), classNameOf(method), nameOf(method), parameterNamesOf(method)))
                                     .append(format("    }%n"))
                                     .toString();
        action.accept(declaration);
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Consumer<String> action) {
        action.accept(format("/* visitExecutable %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitIntersection(IntersectionType t, Consumer<String> action) {
        action.accept(format("/* visitIntersection %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitNoType(NoType t, Consumer<String> action) {
        action.accept(format("/* visitNoType %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitNull(NullType t, Consumer<String> action) {
        action.accept(format("/* visitNull %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitPackage(PackageElement e, Consumer<String> action) {
        action.accept(format("/* visitPackage %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, Consumer<String> action) {
        action.accept(format("/*PrimitiveType*/%s", t));
        return null;
    }

    @Override
    public Void visitType(TypeElement e, Consumer<String> action) {
        action.accept(format("/* visitType %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitTypeParameter(TypeParameterElement e, Consumer<String> action) {
        action.accept(format("/*TypeParameterElement*/%s%s", e.getSimpleName(), boundsOf(e)));
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Consumer<String> action) {
        action.accept(format("/*TypeVariable*/%s%s", t, bound(t.getUpperBound(), "extends")));
        return null;
    }

    @Override
    public Void visitUnion(UnionType t, Consumer<String> action) {
        action.accept(format("/* visitUnion %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitUnknown(Element e, Consumer<String> action) {
        action.accept(format("/* visitUnknown (element) %s (%s) */", e, e.getClass()));
        return null;
    }

    @Override
    public Void visitUnknown(TypeMirror t, Consumer<String> action) {
        action.accept(format("/* visitUnknown (type) %s (%s) */", t, t.getClass()));
        return null;
    }

    @Override
    public Void visitVariable(VariableElement e, Consumer<String> action) {
        StringBuilder typeDeclaration = new StringBuilder();
        TypeMirror type = e.asType();
        if(type instanceof DeclaredType)
            type.accept(this, typeDeclaration::append);
        else
            typeDeclaration.append(type);
        action.accept(format("/*VariableElement*/%s %s", typeDeclaration, e.getSimpleName()));
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Consumer<String> action) {
        String superBound = bound(t.getSuperBound(), "super");
        String extendsBound = bound(t.getExtendsBound(), "extends");
        action.accept(format("/*Wildcard*/?%s%s", superBound, extendsBound));
        return null;
    }

    private String asTypeParameters(Stream<? extends TypeMirror> stream) {
        StringJoiner declaration = new StringJoiner(",", "<", "> ").setEmptyValue("");
        stream.forEach(t -> t.accept(this, declaration::add));
        return declaration.toString();
    }

    private String bound(TypeMirror bound, String prefix) {
        if (bound == null) return "";
        if (Objects.equals(Object.class.getName(), bound.toString())) return "";
        StringBuilder boundType = new StringBuilder();
        bound.accept(this, boundType::append);
        return format(" %s %s", prefix, boundType);
    }

    private String boundsOf(TypeParameterElement e) {
        StringJoiner declaration = new StringJoiner(",", " boundsOf ", "").setEmptyValue("");
        e.getBounds().stream()
                .forEach(b -> b.accept(this, declaration::add));
        return declaration.toString();
    }

    private String commentFor(ExecutableElement method) {
        return Comment.withPrefix(elements.getDocComment(method), "    ");
    }

    private String classNameOf(ExecutableElement method) {
        return method.getEnclosingElement().toString();
    }

    private String exceptionsThrownBy(ExecutableElement method) {
        StringJoiner declaration = new StringJoiner(", ", "throws ", " ").setEmptyValue("");
        method.getThrownTypes().stream()
                .forEach(t -> t.accept(this, declaration::add));
        return declaration.toString();
    }

    private String modifiersOf(ExecutableElement method) {
        return method.getModifiers().stream()
                       .map(String::valueOf)
                       .collect(joining(" "));
    }

    private String nameOf(ExecutableElement method) {
        return method.getSimpleName().toString();
    }

    private String parameterNamesOf(ExecutableElement method) {
        StringJoiner parameterNames = new StringJoiner(", ");
        List<? extends VariableElement> parameters = method.getParameters();
        parameters.stream()
                .map(String::valueOf)
                .forEach(parameterNames::add);
        return parameterNames.toString();
    }

    private String parametersOf(ExecutableElement method) {
        StringJoiner declaration = new StringJoiner(", ");
        method.getParameters()
                .forEach(p -> p.accept(this, declaration::add));
        return declaration.toString();
    }


    private String returnTypeOf(ExecutableElement method) {
        return typeMapper.name(method.getReturnType());
    }

    private String statementFor(ExecutableElement method) {
        return returnTypeOf(method).equals("void") ? "" : "return ";
    }

    private String typeParametersOf(Parameterizable element) {
        return asTypeParameters(element.getTypeParameters().stream().map(Element::asType));
    }

    private String typeParametersOf(DeclaredType element) {
        return asTypeParameters(element.getTypeArguments().stream());
    }
}
