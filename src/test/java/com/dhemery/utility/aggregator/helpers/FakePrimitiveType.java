package com.dhemery.utility.aggregator.helpers;

import javax.lang.model.type.*;

public class FakePrimitiveType extends FakeTypeMirror implements PrimitiveType {
    public FakePrimitiveType(TypeKind kind) {
        super(kind);
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitPrimitive(this, p);
    }
}
