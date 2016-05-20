package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.*;
import org.junit.*;

import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static com.dhemery.aggregator.helpers.TypeVisitorTour.returnType;
import static com.dhemery.aggregator.helpers.TypeVisitorTour.visitEach;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodWriterVisitDeclaredTypeTests {
    public MethodWriter writer;

    @Before
    public void setup() {
        writer = new MethodWriter(new FullTypeReferences());
    }

    @Test
    public <P> void simpleDeclaredType() {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .build();

        StringBuilder declaration = new StringBuilder();

        visitEach(sourceFile, TestTarget.class, returnType())
                .with(writer, declaration::append);

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }

    @Test
    @Ignore("test itself is wip")
    public void genericDeclaredType() {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static <T extends java.nio.file.Path> T makeAPath() { return null; }")
                                        .build();

        StringBuilder declaration = new StringBuilder();

        visitEach(sourceFile, TestTarget.class, returnType())
                .with(writer, declaration::append);

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }
}
