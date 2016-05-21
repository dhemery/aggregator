package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.SourceFile;
import com.dhemery.aggregator.helpers.TestTarget;
import org.junit.Test;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

import static com.dhemery.aggregator.helpers.CompilationBuilder.process;
import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFile;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TypeScannerTests {
    private final TypeScanner scanner = new TypeScanner();

    @Test
    public void reportsDeclaredReturnReturnType() {
        SourceFile sourceFile = sourceFile()
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .forClass("Simple");

        Set<String> scannedTypes = new HashSet<>();

        process(TestTarget.class).in(sourceFile).by(
                re -> re.getElementsAnnotatedWith(TestTarget.class).stream()
                              .map(ExecutableElement.class::cast)
                              .map(ExecutableElement::getReturnType)
                              .forEach(t -> t.accept(scanner, scannedTypes::add)));

        assertThat(scannedTypes, containsInAnyOrder("java.nio.file.Path"));
    }
}
