package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.FakeNoType;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.function.Consumer;

public class MethodWriterVisitVoidTypeTest {
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
}
