package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Describe extends SimpleTypeVisitor8<List<String>,List<String>> {
    public static String the(TypeMirror type) {
        return type.accept(new Describe(), new ArrayList<>()).stream().map(l -> format("// %s", l)).collect(joining(System.lineSeparator()));
    }

    @Override
    protected List<String> defaultAction(TypeMirror e, List<String> strings) {
        strings.add(format("default action for %s (%s)", e, e.getClass()));
        return strings;
    }

    @Override
    public List<String> visitNoType(NoType t, List<String> strings) {
        strings.add(format("visiting NoType %s", t));
        return strings;
    }

    @Override
    public List<String> visitPrimitive(PrimitiveType t, List<String> strings) {
        strings.add(format("visiting PrimitiveType %s", t));
        return strings;
    }

    @Override
    public List<String> visitNull(NullType t, List<String> strings) {
        strings.add(format("visiting Null %s", t));
        return strings;
    }

    @Override
    public List<String> visitArray(ArrayType t, List<String> strings) {
        strings.add(format("visiting ArrayType %s", t));
        return strings;
    }

    @Override
    public List<String> visitDeclared(DeclaredType t, List<String> strings) {
        strings.add(format("visiting DeclaredType %s", t));
        return strings;
    }

    @Override
    public List<String> visitError(ErrorType t, List<String> strings) {
        strings.add(format("visiting ErrorType %s", t));
        return strings;
    }

    @Override
    public List<String> visitTypeVariable(TypeVariable t, List<String> strings) {
        strings.add(format("visiting TypeVariable %s", t));
        t.getLowerBound().accept(this, strings);
        t.getUpperBound().accept(this, strings);
        return strings;
    }

    @Override
    public List<String> visitWildcard(WildcardType t, List<String> strings) {
        strings.add(format("visiting WildcardType %s", t));
        return strings;
    }

    @Override
    public List<String> visitExecutable(ExecutableType t, List<String> strings) {
        strings.add(format("visiting ExecutableType %s", t));
        return strings;
    }

    @Override
    public List<String> visitUnknown(TypeMirror t, List<String> strings) {
        strings.add(format("visiting Unknown %s (kind: %s) (class: %s)", t, t.getKind(), t.getClass()));
        return strings;
    }
}
