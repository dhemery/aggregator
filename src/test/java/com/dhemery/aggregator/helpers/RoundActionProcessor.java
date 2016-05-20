package com.dhemery.aggregator.helpers;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class RoundActionProcessor extends AbstractProcessor {
    private final RoundAction roundAction;
    private final SourceVersion supportedSourceVersion;
    private final Set<String> supportedAnnotationTypeNames;

    @SafeVarargs
    public RoundActionProcessor(RoundAction roundAction, SourceVersion supportedSourceVersion, Class<? extends Annotation>... supportedAnnotationTypes) {
        this.roundAction = roundAction;
        this.supportedSourceVersion = supportedSourceVersion;
        this.supportedAnnotationTypeNames = Stream.of(supportedAnnotationTypes)
                                                    .map(Class::getCanonicalName)
                                                    .collect(toSet());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        return roundAction.process(annotations, roundEnvironment, processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypeNames;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return supportedSourceVersion;
    }
}
