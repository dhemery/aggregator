package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.*;
import org.junit.*;

import javax.lang.model.element.ExecutableElement;

import static com.dhemery.aggregator.helpers.CompilationBuilder.process;
import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFile;
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
    public void simpleDeclaredType() {
        SourceFile sourceFile = sourceFile()
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .forClass("Simple");

        StringBuilder declaration = new StringBuilder();

        process(TestTarget.class).in(sourceFile).by(
                re -> re.getElementsAnnotatedWith(TestTarget.class).stream()
                              .map(ExecutableElement.class::cast)
                              .map(ExecutableElement::getReturnType)
                              .forEach(t -> t.accept(writer, declaration::append)));

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }

    @Test
    @Ignore("test is wip")
    public void genericDeclaredType() {
        SourceFile sourceFile = sourceFile()
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static <T extends java.nio.file.Path> T makeAPath() { return null; }")
                                        .forClass("Simple");

        StringBuilder declaration = new StringBuilder();

        process(TestTarget.class).in(sourceFile).by(
                re -> re.getElementsAnnotatedWith(TestTarget.class).stream()
                              .map(ExecutableElement.class::cast)
                              .map(ExecutableElement::getReturnType)
                              .forEach(t -> t.accept(writer, declaration::append)));

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }
}
