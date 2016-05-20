package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.aggregator.helpers.ProjectBuilder.project;

public class TypeVisitorTour {
    private final Predicate<Element> elementFilter;
    private final Function<Element, TypeMirror> elementMapper;
    private final Class<? extends Annotation> annotationType;
    private final SourceFile sourceFile;

    private TypeVisitorTour(SourceFile sourceFile, Class<? extends Annotation> annotationType, Predicate<Element> elementFilter, Function<Element, TypeMirror> elementMapper) {
        this.sourceFile = sourceFile;
        this.annotationType = annotationType;
        this.elementFilter = elementFilter;
        this.elementMapper = elementMapper;
    }

    public static TypeVisitorTour visitEach(SourceFile sourceFile, Class<? extends Annotation> annotationType, Predicate<Element> elementFilter, Function<Element, TypeMirror> elementMapper) {
        return new TypeVisitorTour(sourceFile, annotationType, elementFilter, elementMapper);
    }

    public static TypeVisitorTour visitEach(SourceFile sourceFile, Class<? extends Annotation> annotationType, Function<Element, TypeMirror> elementMapper) {
        return visitEach(sourceFile, annotationType, e -> true, elementMapper);
    }

    public <R, P> List<R> with(TypeVisitor<R, P> visitor, P input) throws IOException {
        List<R> output = new ArrayList<>();
        RoundAction roundAction = roundAction(annotationType, elementFilter, elementMapper, visitor, input, output);
        process(sourceFile, annotationType, roundAction);
        return output;
    }

    private static <R, P> RoundAction roundAction(Class<? extends Annotation> annotationType,
                                                  Predicate<? super Element> elementFilter,
                                                  Function<? super Element, ? extends TypeMirror> elementMapper,
                                                  TypeVisitor<R, P> visitor,
                                                  P input,
                                                  List<R> output) {
        return (annotations, roundEnvironment, processingEnvironment) -> {
            roundEnvironment.getElementsAnnotatedWith(annotationType).stream()
                    .filter(elementFilter)
                    .map(elementMapper)
                    .forEach(t -> output.add(t.accept(visitor, input)));
            return false;
        };
    }

    public static boolean process(SourceFile sourceFile, Class<? extends Annotation> annotationType, RoundAction roundAction) throws IOException {
        Processor processor = new RoundActionProcessor(roundAction, SourceVersion.latestSupported(), annotationType);
        return project()
                       .withSourceFile(sourceFile)
                       .withProcessor(processor)
                       .compile();
    }

    public static Function<Element, TypeMirror> returnType() {
        return e -> ((ExecutableElement) e).getReturnType();
    }
}
