package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.SourceFile;
import com.dhemery.aggregator.helpers.TestTarget;
import org.junit.Test;

import javax.lang.model.element.ExecutableElement;
import java.io.IOException;

import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MethodWriterVisitDeclaredTypeTest extends MethodWriterTestBase {

    @Test
    public void simpleDeclaredType() throws IOException {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .build();

        StringBuilder declaration = new StringBuilder();

        process(sourceFile, by(visiting(ExecutableElement::getReturnType, declaration::append)));

        assertThat(declaration, is("java.nio.file.Path"));
    }

}
