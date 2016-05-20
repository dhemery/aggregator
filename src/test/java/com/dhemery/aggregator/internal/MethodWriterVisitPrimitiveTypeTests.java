package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static com.dhemery.aggregator.helpers.TypeVisitorTour.returnType;
import static com.dhemery.aggregator.helpers.TypeVisitorTour.visitEach;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class MethodWriterVisitPrimitiveTypeTests {
    public MethodWriter writer;

    @Before
    public void setup() {
        writer = new MethodWriter(new FullTypeReferences());
    }

    @Parameter
    public TestType type;

    @Parameters(name = "{0}")
    public static Collection<TestType> types() {
        return Arrays.asList(
                new TestType("void", ""),
                new TestType("byte", "return 0;"),
                new TestType("char", "return 0;"),
                new TestType("short", "return 0;"),
                new TestType("long", "return 0;"),
                new TestType("float", "return 0;"),
                new TestType("double", "return 0;")
        );
    }

    @Test
    public void primitiveType() {
        SourceFile sourceFile = sourceFileForClass("PrimitiveReturnType")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine(format("public static %s returnsPrimitiveType() { %s }", type.name, type.returnStatement))
                                        .build();

        StringBuilder declaration = new StringBuilder();

        visitEach(sourceFile, TestTarget.class, returnType())
                .with(writer, declaration::append);

        assertThat(declaration.toString(), is(type.name));
    }

    private static class TestType {
        final String name;
        final String returnStatement;

        TestType(String name, String returnStatement) {
            this.name = name;
            this.returnStatement = returnStatement;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
