package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.Consumers;
import com.dhemery.aggregator.helpers.FakeNoType;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.lang.model.type.*;
import java.util.function.Consumer;

public class MethodWriterVisitNoType {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private Consumer<String> declaration;
    private final MethodWriter writer = new MethodWriter(null);

    @Test
    public void voidType() {
        TypeKind kind = TypeKind.VOID;
        TypeMirror voidType = new FakeNoType(kind);

        context.checking(new Expectations() {{
            oneOf(declaration).accept(kind.toString().toLowerCase());
        }});

        writer.visit(voidType, declaration);
    }

    @Test(expected = UnknownTypeException.class)
    public void packageTypeThrowsInsteadOfDeclaring() {
        TypeMirror packageType = new FakeNoType(TypeKind.PACKAGE);

        writer.visit(packageType, Consumers::doNotCall);
    }

    @Test(expected = UnknownTypeException.class)
    public void noneTypeThrowsInsteadOfDeclaring() {
        TypeMirror packageType = new FakeNoType(TypeKind.NONE);

        writer.visit(packageType, Consumers::doNotCall);
    }
}
