package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.Optional;

class TypeWriter extends SimpleTypeVisitor8<StringBuilder, StringBuilder> {
    private final TypeMapper typeMapper;

    TypeWriter(TypeMapper typeMapper) {
        super(new StringBuilder().append("bogus"));
        this.typeMapper = typeMapper;
    }

    String declare(TypeMirror type) {
        return type.accept(this, new StringBuilder()).toString();
    }

    @Override
    public StringBuilder visitDeclared(DeclaredType t, StringBuilder declaration) {
        return declaration.append(typeMapper.apply(t));
    }

    @Override
    public StringBuilder visitPrimitive(PrimitiveType t, StringBuilder declaration) {
        return declaration.append(t);
    }

    @Override
    public StringBuilder visitTypeVariable(TypeVariable t, StringBuilder declaration) {
        return declaration
                       .append(t)
                       .append(bound(t.getUpperBound(), " extends "));
    }

    private String bound(TypeMirror bound, String prefix) {
        return Optional.of(bound)
                       .filter(b -> !Object.class.getName().equals(b.toString()))
                       .map(b -> b.accept(this, new StringBuilder().append(prefix)))
                       .map(String::valueOf)
                       .orElse("");
    }
}
