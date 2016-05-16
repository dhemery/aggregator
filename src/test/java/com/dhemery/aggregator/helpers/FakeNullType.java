package com.dhemery.aggregator.helpers;

import javax.lang.model.type.*;

public class FakeNullType extends FakeTypeMirror implements NullType {
    public FakeNullType() {
        super(TypeKind.NULL);
    }


    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        return v.visitNull(this, p);
    }
}
