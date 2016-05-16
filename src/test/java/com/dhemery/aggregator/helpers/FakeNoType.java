package com.dhemery.aggregator.helpers;

import javax.lang.model.type.*;

public class FakeNoType implements FakeAnnotatedConstruct, NoType {
    private final TypeKind kind;

    public FakeNoType(TypeKind kind) {
        this.kind = kind;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitNoType(this, p);
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }
}
