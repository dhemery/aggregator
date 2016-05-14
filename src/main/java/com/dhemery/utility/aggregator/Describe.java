package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.StringJoiner;

import static java.lang.String.format;

public class Describe extends SimpleTypeVisitor8<StringJoiner, StringJoiner> {
    public static String the(TypeMirror type) {
        StringJoiner description = new StringJoiner(format("%n// ")).add("");
        return type.accept(new Describe(), description).toString();
    }

    @Override
    protected StringJoiner defaultAction(TypeMirror e, StringJoiner strings) {
        return strings.add(format("default action for %s (%s)", e, e.getClass()));
    }

    @Override
    public StringJoiner visitNoType(NoType t, StringJoiner strings) {
        return strings.add(format("visiting NoType %s", t));
    }

    @Override
    public StringJoiner visitPrimitive(PrimitiveType t, StringJoiner strings) {
        return strings.add(format("visiting PrimitiveType %s", t));
    }

    @Override
    public StringJoiner visitNull(NullType t, StringJoiner strings) {
        return strings.add(format("visiting Null %s", t));
    }

    @Override
    public StringJoiner visitArray(ArrayType t, StringJoiner strings) {
        return strings.add(format("visiting ArrayType %s", t));
    }

    @Override
    public StringJoiner visitDeclared(DeclaredType t, StringJoiner strings) {
        return strings.add(format("visiting DeclaredType %s", t));
    }

    @Override
    public StringJoiner visitError(ErrorType t, StringJoiner strings) {
        return strings.add(format("visiting ErrorType %s", t));
    }

    @Override
    public StringJoiner visitTypeVariable(TypeVariable t, StringJoiner strings) {
        strings.add(format("visiting TypeVariable %s", t));
        t.getLowerBound().accept(this, strings);
        t.getUpperBound().accept(this, strings);
        return strings;
    }

    @Override
    public StringJoiner visitWildcard(WildcardType t, StringJoiner strings) {
        return strings.add(format("visiting WildcardType %s", t));
    }

    @Override
    public StringJoiner visitExecutable(ExecutableType t, StringJoiner strings) {
        return strings.add(format("visiting ExecutableType %s", t));
    }

    @Override
    public StringJoiner visitUnknown(TypeMirror t, StringJoiner strings) {
        return strings.add(format("visiting Unknown %s (kind: %s) (class: %s)", t, t.getKind(), t.getClass()));
    }
}
