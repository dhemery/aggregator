package com.dhemery.aggregator.helpers;

import javax.lang.model.type.*;

public class FakeNoType extends FakeTypeMirror implements NoType {
    public FakeNoType(TypeKind kind) {
        super(kind);
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitNoType(this, p);
    }
}
