package com.dhemery.aggregator.helpers;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class FakeTypeElement extends FakeElement implements TypeElement {

    public FakeTypeElement(String simpleName) {
        super(simpleName);
    }

    @Override
    public NestingKind getNestingKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getQualifiedName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypeMirror getSuperclass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        throw new UnsupportedOperationException();
    }

}
