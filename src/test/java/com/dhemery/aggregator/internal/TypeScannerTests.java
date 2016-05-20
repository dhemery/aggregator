package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.SourceFile;
import com.dhemery.aggregator.helpers.TestTarget;
import org.junit.Test;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Set;

import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static com.dhemery.aggregator.helpers.TypeVisitorTour.visitEach;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TypeScannerTests {
    private final TypeScanner scanner = new TypeScanner();

    @Test
    public void reportsDeclaredReturnReturnType() {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine(format("@%s", TestTarget.class.getName()))
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .build();

        Set<String> scannedTypes = new HashSet<>();

        visitEach(sourceFile, TestTarget.class, Element::asType)
                .with(scanner, scannedTypes::add);

        assertThat(scannedTypes, containsInAnyOrder("java.nio.file.Path"));
    }
}
