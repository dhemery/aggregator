package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ProcessorBuilder {
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();
    private SourceVersion supportedSourceVersion = SourceVersion.latestSupported();

    private ProcessorBuilder() {
    }

    public static ProcessorBuilder processor() {
        return new ProcessorBuilder();
    }

    public ProcessorBuilder supporting(Class<? extends Annotation>... annotationTypes) {
        return supporting(Stream.of(annotationTypes).map(Class::getCanonicalName).collect(toSet()));
    }

    public ProcessorBuilder supporting(Set<String> annotationTypeNames) {
        this.supportedAnnotationTypeNames.addAll(annotationTypeNames);
        return this;
    }

    public ProcessorBuilder supporting(String... annotationTypeNames) {
        return supporting(Stream.of(annotationTypeNames).collect(toSet()));
    }

    public ProcessorBuilder supporting(SourceVersion sourceVersion) {
        supportedSourceVersion = sourceVersion;
        return this;
    }

    public Processor onEachRound(RoundAction action) {
        return new RoundActionProcessor(action, supportedSourceVersion, supportedAnnotationTypeNames);
    }
}
