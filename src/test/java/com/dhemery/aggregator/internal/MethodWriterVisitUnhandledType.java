package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.*;
import org.junit.Test;

import javax.lang.model.type.*;

public class MethodWriterVisitUnhandledType {
    private final MethodWriter writer = new MethodWriter(null);

    @Test(expected = UnknownTypeException.class)
    public void noneNoTypeThrowsInsteadOfDeclaring() {
        TypeMirror packageType = new FakeNoType(TypeKind.NONE);

        writer.visit(packageType, Consumers::doNotCall);
    }

    @Test(expected = UnknownTypeException.class)
    public void packageNoTypeThrowsInsteadOfDeclaring() {
        TypeMirror packageType = new FakeNoType(TypeKind.PACKAGE);

        writer.visit(packageType, Consumers::doNotCall);
    }

    @Test(expected = UnknownTypeException.class)
    public void nullTypeThrowsInsteadOfDeclaring() {
        TypeMirror packageType = new FakeNullType();

        writer.visit(packageType, Consumers::doNotCall);
    }
}
