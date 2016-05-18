package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.RoundProcessor;
import com.dhemery.aggregator.helpers.SourceFile;
import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.*;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processor;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;
import static com.dhemery.aggregator.helpers.SourceFileBuilder.sourceFileForClass;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TypeScannerTests {
    private final TypeScanner scanner = new TypeScanner();

    @Test
    public void reportsDeclaredReturnReturnType() throws IOException {
        SourceFile sourceFile = sourceFileForClass("Simple")
                                        .withLine("public static java.nio.file.Path makeAPath() { return null; }")
                                        .build();

        Set<String> scannedTypes = scanTypesUsedIn(sourceFile);

        assertThat(scannedTypes, containsInAnyOrder("java.nio.file.Path"));
    }

    private Set<String> scanTypesUsedIn(SourceFile sourceFile) throws IOException {
        Set<String> scannedTypes = new HashSet<>();

        processSourceFile(
                sourceFile,
                processingBy(scanningMethods(scanner, scannedTypes)));

        return scannedTypes;
    }

    private void processSourceFile(SourceFile sourceFile, Processor processor) throws IOException {
        project()
                .withSourceFile(sourceFile)
                .withProcessor(processor)
                .compile();
    }

    private Processor processingBy(RoundProcessor roundProcessor) {
        return processor()
                       .supportingAnnotationTypes("*")
                       .performingOnEachRound(roundProcessor)
                       .build();
    }

    private RoundProcessor scanningMethods(TypeScanner scanned, Set<String> scannedTypes) {
        return (annotations, roundEnvironment, processingEnv) -> {
            methodTypesIn(roundEnvironment)
                    .forEach(e -> e.accept(scanned, scannedTypes::add));
            return false;
        };
    }

    private List<TypeMirror> methodTypesIn(RoundEnvironment roundEnvironment) {
        return methodElementsIn(roundEnvironment).stream()
                       .map(Element::asType)
                       .collect(toList());
    }

    private List<ExecutableElement> methodElementsIn(RoundEnvironment roundEnvironment) {
        return rootElementsIn(roundEnvironment).stream()
                       .flatMap(e -> ElementFilter.methodsIn(e.getEnclosedElements()).stream())
                       .collect(toList());
    }

    private Set<? extends Element> rootElementsIn(RoundEnvironment roundEnvironment) {
        return roundEnvironment.getRootElements();
    }
}
