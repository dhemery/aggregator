package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processor;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;
import static java.util.stream.Collectors.toSet;

public class CompilationBuilder {
    private final Set<SourceFile> sourceFiles = new HashSet<>();
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();

    private CompilationBuilder() {
    }

    public static CompilationBuilder process() {
        return new CompilationBuilder();
    }

    @SafeVarargs
    final public CompilationBuilder annotations(Class<? extends Annotation>... annotationTypes) {
        Stream.of(annotationTypes)
                .map(Class::getCanonicalName)
                .forEach(supportedAnnotationTypeNames::add);
        return this;
    }

    public CompilationBuilder annotations(String... annotationTypeNames) {
        Stream.of(annotationTypeNames)
                .forEach(supportedAnnotationTypeNames::add);
        return this;
    }

    public CompilationBuilder in(SourceFile... sourceFiles) {
        return in(Stream.of(sourceFiles).collect(toSet()));
    }

    public CompilationBuilder in(Set<SourceFile> sourceFiles) {
        this.sourceFiles.addAll(sourceFiles);
        return this;
    }

    public boolean by(RoundAction action) {
        Processor processor = processor()
                                      .supporting(supportedAnnotationTypeNames)
                                      .onEachRound(action);
        return project()
                       .with(sourceFiles)
                       .compileWith(processor);
    }

    public boolean by(Consumer<RoundEnvironment> consumer) {
        return by((annotations, roundEnvironment, processingEnvironment) -> {
            consumer.accept(roundEnvironment);
            return false;
        });
    }
}
