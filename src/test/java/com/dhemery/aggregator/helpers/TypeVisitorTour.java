package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processor;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;

public class TypeVisitorTour {
    private final Predicate<Element> elementFilter;
    private final Function<Element, TypeMirror> elementMapper;
    private final Class<? extends Annotation> annotationType;
    private final SourceFile sourceFile;

    public static TypeVisitorTour visitEach(SourceFile sourceFile, Class<? extends Annotation> annotationType, Predicate<Element> elementFilter, Function<Element, TypeMirror> elementMapper) {
        return new TypeVisitorTour(sourceFile, annotationType, elementFilter, elementMapper);
    }

    public static TypeVisitorTour visitEach(SourceFile sourceFile, Class<? extends Annotation> annotationType, Function<Element, TypeMirror> elementMapper) {
        return visitEach(sourceFile, annotationType, e -> true, elementMapper);
    }

    public static TypeVisitorTour visitEach(SourceFile sourceFile, Predicate<Element> elementFilter, Function<Element, TypeMirror> elementMapper) {
        return visitEach(sourceFile, null, elementFilter, elementMapper);
    }

    public <R, P> List<R> with(TypeVisitor<R, P> visitor, P input) throws IOException {
        List<R> output = new ArrayList<>();
        projectProcessedBy(
                performing(
                        visitBy(visitor, input, output),
                        onEachTargetType()))
                .compile();
        return output;
    }

    public static Function<Element, TypeMirror> returnType() {
        return e -> ((ExecutableElement) e).getReturnType();
    }

    private TypeVisitorTour(SourceFile sourceFile, Class<? extends Annotation> annotationType, Predicate<Element> elementFilter, Function<Element, TypeMirror> elementMapper) {
        this.sourceFile = sourceFile;
        this.annotationType = annotationType;
        this.elementFilter = elementFilter;
        this.elementMapper = elementMapper;
    }

    private Processor performing(RoundProcessor roundAction, String targetAnnotationTypes) {
        return processor()
                       .supportingAnnotationTypes(targetAnnotationTypes)
                       .performingOnEachRound(roundAction)
                       .build();
    }

    private String onEachTargetType() {
        return Optional.ofNullable(annotationType)
                       .map(Class::getCanonicalName)
                       .orElse("*");
    }

    private <R, P> RoundProcessor visitBy(TypeVisitor<R, P> visitor, P action, List<R> results) {
        return (annotations, roundEnvironment, processingEnvironment) -> {
            targetElements().apply(roundEnvironment).stream()
                    .filter(elementFilter)
                    .map(elementMapper)
                    .forEach(t -> results.add(visitor.visit(t, action)));
            return true;
        };
    }

    private Function<RoundEnvironment, Set<? extends Element>> targetElements() {
        if (annotationType == null) return RoundEnvironment::getRootElements;
        return re -> re.getElementsAnnotatedWith(annotationType);
    }

    private ProjectBuilder projectProcessedBy(Processor processor) {
        return project()
                       .withSourceFile(sourceFile)
                       .withProcessor(processor);
    }
}
