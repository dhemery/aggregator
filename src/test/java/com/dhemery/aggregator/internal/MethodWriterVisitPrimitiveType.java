package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.FakePrimitiveType;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

@RunWith(Parameterized.class)
public class MethodWriterVisitPrimitiveType {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private Consumer<String> declaration;
    private final MethodWriter writer = new MethodWriter(null);

    @Parameter
    public TypeKind kind;

    @Parameters(name = "{0}")
    public static Collection<TypeKind> types() {
        return Arrays.asList(
                TypeKind.BOOLEAN,
                TypeKind.BYTE,
                TypeKind.CHAR,
                TypeKind.DOUBLE,
                TypeKind.FLOAT,
                TypeKind.INT,
                TypeKind.LONG,
                TypeKind.SHORT
        );
    }

    @Test
    public void primitiveType() {
        TypeMirror primitiveType = new FakePrimitiveType(kind);

        context.checking(new Expectations() {{
            oneOf(declaration).accept(kind.name().toLowerCase());
        }});

        writer.visit(primitiveType, declaration);
    }
}
