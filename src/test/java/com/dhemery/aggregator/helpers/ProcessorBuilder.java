package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ProcessorBuilder {
    private RoundProcessor roundProcessor = (annotations, roundEnv, processingEnv) -> false;
    private Set<String> supportedAnnotationTypes = new HashSet<>();
    private SourceVersion supportedSourceVersion = SourceVersion.RELEASE_8;

    public static ProcessorBuilder processor() {
        return new ProcessorBuilder();
    }

    public ProcessorBuilder performingOnEachRound(RoundProcessor roundProcessor) {
        this.roundProcessor = roundProcessor;
        return this;
    }

    public ProcessorBuilder supportingAnnotationTypes(Class<?>... types) {
        Stream.of(types)
                .map(Class::getCanonicalName)
                .forEach(supportedAnnotationTypes::add);
        return this;
    }

    public ProcessorBuilder supportingAnnotationTypes(String... fullyQualifiedTypeNames) {
        Stream.of(fullyQualifiedTypeNames)
                .forEach(supportedAnnotationTypes::add);
        return this;
    }

    public Processor build() {
        return new ComposedProcessor(roundProcessor, supportedSourceVersion, supportedAnnotationTypes);
    }
}
