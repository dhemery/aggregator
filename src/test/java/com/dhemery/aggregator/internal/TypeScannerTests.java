package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.SourceFile;
import com.dhemery.aggregator.helpers.TestTarget;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.dhemery.aggregator.helpers.ProcessorUtils.RETURN_TYPE;
import static com.dhemery.aggregator.helpers.ProcessorUtils.each;
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

        each(sourceFile, TestTarget.class, RETURN_TYPE, t -> t.accept(scanner, scannedTypes::add));

        assertThat(scannedTypes, containsInAnyOrder("java.nio.file.Path"));
    }
}
