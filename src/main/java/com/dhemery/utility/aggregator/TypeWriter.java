package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.Optional;

class TypeWriter extends SimpleTypeVisitor8<StringBuilder, StringBuilder> {
    String declare(TypeMirror type) {
        return type.accept(this, new StringBuilder()).toString();
    }

    @Override
    protected StringBuilder defaultAction(TypeMirror e, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitArray(ArrayType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitDeclared(DeclaredType t, StringBuilder declaration) {
        return declaration.append(String.valueOf(t));
    }

    @Override
    public StringBuilder visitError(ErrorType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitExecutable(ExecutableType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitNoType(NoType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitNull(NullType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitPrimitive(PrimitiveType t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitTypeVariable(TypeVariable t, StringBuilder declaration) {
        return declaration
                       .append(t)
                       .append(bound(t.getUpperBound(), " extends "));
    }

    @Override
    public StringBuilder visitUnknown(TypeMirror t, StringBuilder declaration) {
        return declaration;
    }

    @Override
    public StringBuilder visitWildcard(WildcardType t, StringBuilder declaration) {
        return declaration;
    }

    private String bound(TypeMirror bound, String prefix) {
        return Optional.of(bound)
                       .filter(b -> !Object.class.getName().equals(b.toString()))
                       .map(b -> b.accept(this, new StringBuilder().append(prefix)))
                       .map(String::valueOf)
                       .orElse("");
    }
}
