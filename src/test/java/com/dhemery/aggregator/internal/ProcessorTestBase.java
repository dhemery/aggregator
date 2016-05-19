package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.*;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import java.io.IOException;
import java.util.Set;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processor;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;
import static java.util.stream.Collectors.toSet;

class ProcessorTestBase {
    void process(SourceFile sourceFile, Processor processor) throws IOException {
        project()
                .withSourceFile(sourceFile)
                .withProcessor(processor)
                .compile();
    }

    Processor by(RoundProcessor roundProcessor) {
        return processor()
                       .supportingAnnotationTypes(TestTarget.class)
                       .performingOnEachRound(roundProcessor)
                       .build();
    }

    Set<ExecutableElement> testTargetElements(RoundEnvironment roundEnvironment) {
        return roundEnvironment.getElementsAnnotatedWith(TestTarget.class).stream()
                       .map(ExecutableElement.class::cast)
                       .collect(toSet());
    }
}
