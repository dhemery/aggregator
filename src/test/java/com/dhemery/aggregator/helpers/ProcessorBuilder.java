package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static com.dhemery.aggregator.helpers.Streams.streamOf;

public class ProcessorBuilder {
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();
    private SourceVersion supportedSourceVersion = SourceVersion.latestSupported();

    private ProcessorBuilder() {
    }

    public static ProcessorBuilder processorSupporting(String firstAnnotationTypeName, String... otherAnnotationTypeNames) {
        return new ProcessorBuilder()
                       .supporting(firstAnnotationTypeName, otherAnnotationTypeNames);
    }

    @SafeVarargs
    public static ProcessorBuilder processorSupporting(Class<? extends Annotation> first, Class<? extends Annotation>... others) {
        return new ProcessorBuilder()
                       .supporting(first, others);
    }

    public ProcessorBuilder supporting(Class<? extends Annotation> first, Class<? extends Annotation>... others) {
        streamOf(first, others)
                .map(Class::getCanonicalName)
                .forEach(supportedAnnotationTypeNames::add);
        return this;
    }

    public ProcessorBuilder supporting(String first, String... others) {
        streamOf(first, others)
                .forEach(supportedAnnotationTypeNames::add);
        return this;
    }

    public ProcessorBuilder supporting(SourceVersion sourceVersion) {
        supportedSourceVersion = sourceVersion;
        return this;
    }

    public Processor onEachRound(RoundAction action) {
        return new RoundActionProcessor(action, supportedSourceVersion, supportedAnnotationTypeNames);
    }
}
