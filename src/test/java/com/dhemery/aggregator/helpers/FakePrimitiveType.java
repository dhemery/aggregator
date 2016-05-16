package com.dhemery.aggregator.helpers;

import javax.lang.model.type.*;

public class FakePrimitiveType implements FakeAnnotatedConstruct, PrimitiveType {
    private final TypeKind kind;

    public FakePrimitiveType(TypeKind kind) {
        this.kind = kind;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitPrimitive(this, p);
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }
}
