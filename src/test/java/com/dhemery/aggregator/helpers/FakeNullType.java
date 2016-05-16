package com.dhemery.aggregator.helpers;

import javax.lang.model.type.*;

public class FakeNullType implements FakeAnnotatedConstruct, NullType {
    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitNull(this, p);
    }

    @Override
    public TypeKind getKind() {
        return TypeKind.NULL;
    }
}
