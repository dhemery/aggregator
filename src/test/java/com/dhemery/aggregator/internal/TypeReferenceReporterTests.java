package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.RoundProcessor;
import com.dhemery.aggregator.helpers.SourceFile;
import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.*;

import static com.dhemery.aggregator.helpers.Processors.processor;
import static com.dhemery.aggregator.helpers.Project.project;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TypeReferenceReporterTests {
    private final TypeReferenceReporter reporter = new TypeReferenceReporter();

    @Test
    public void foo() throws IOException {
        String method = "public static java.nio.file.Path makeAPath() { return null; }";
        List<String> code = Arrays.asList(
                "public class Simple {",
                method,
                "}"
        );

        SourceFile sourceFile = new SourceFile("Simple.java", code);

        Set<String> reportedTypes = reportTypesUsedIn(sourceFile);

        assertThat(reportedTypes, containsInAnyOrder("java.nio.file.Path"));
    }

    private Set<String> reportTypesUsedIn(SourceFile sourceFile) throws IOException {
        Set<String> reportedTypes = new HashSet<>();

        RoundProcessor roundAction = reportTypesUsedInMethods(reporter, reportedTypes);

        Processor processor = processor()
                                      .supportingAnnotationTypes("*")
                                      .onEachRound(roundAction)
                                      .build();

        project()
                .withSourceFile(sourceFile)
                .withProcessor(processor)
                .compile();

        return reportedTypes;
    }

    private RoundProcessor reportTypesUsedInMethods(TypeReferenceReporter reporter, Set<String> reportedTypes) {
        return (annotations, roundEnvironment, processingEnv) -> {
            roundEnvironment.getRootElements().stream()
                    .flatMap(e -> ElementFilter.methodsIn(e.getEnclosedElements()).stream())
                    .map(Element::asType)
                    .forEach(e -> e.accept(reporter, reportedTypes::add));
            return false;
        };
    }
}
