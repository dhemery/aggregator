package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.SourceFile;
import com.dhemery.aggregator.helpers.TestTarget;
import org.junit.Ignore;
import org.junit.Test;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.function.Function;

import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodWriterVisitDeclaredTypeTests extends MethodWriterTestBase {

    @Test
    public void simpleDeclaredType() throws IOException {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .build();

        StringBuilder declaration = new StringBuilder();

        process(sourceFile, by(withEachTypeIn(ExecutableElement::getReturnType, declaration::append)));

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }

    @Test
    @Ignore("test itself is wip")
    public void genericDeclaredType() throws IOException {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static <T extends java.nio.file.Path> T makeAPath() { return null; }")
                                        .build();

        StringBuilder declaration = new StringBuilder();

        Function<ExecutableElement, TypeMirror> returnType = ExecutableElement::getReturnType;
        process(sourceFile, by(withEachTypeIn(returnType, declaration::append)));

        assertThat(declaration.toString(), is("java.nio.file.Path"));
    }
}
