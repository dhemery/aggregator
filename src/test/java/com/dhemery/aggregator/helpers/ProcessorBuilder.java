package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.dhemery.aggregator.helpers.ProjectBuilder.project;
import static com.dhemery.aggregator.helpers.Streams.streamOf;

public class ProcessorBuilder {
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();
    private SourceVersion supportedSourceVersion = SourceVersion.latestSupported();

    private ProcessorBuilder() {
    }

    @SafeVarargs
    public static ProcessorBuilder processorSupporting(Class<? extends Annotation> first, Class<? extends Annotation>... others) {
        return new ProcessorBuilder()
                       .supporting(first, others);
    }

    public static Processor processorAcceptingEach(Class<? extends Annotation> targetAnnotationType, Consumer<? super Element> consumer) {
        return processorSupporting(targetAnnotationType)
                       .onEachRound(consumingEach(targetAnnotationType, consumer));
    }

    private static RoundAction consumingEach(Class<? extends Annotation> targetAnnotationType, Consumer<? super Element> consumer) {
        return (ignoredAnnotations, roundEnvironment, ignoredProcessingEnvironment) -> {
            roundEnvironment.getElementsAnnotatedWith(targetAnnotationType).stream()
                    .forEach(consumer);
            return false;
        };
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

    public static void each(SourceFile sourceFile,
                            Class<? extends Annotation> targetAnnotationType,
                            Function<? super Element, ? extends TypeMirror> mapper,
                            Consumer<? super TypeMirror> consumer) {

        Processor processor = processorAcceptingEach(targetAnnotationType,
                e -> consumer.accept(mapper.apply(e)));
        project()
                .with(sourceFile)
                .compileWith(processor);
    }
}
