package com.dhemery.aggregator.helpers;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class RoundActionProcessor extends AbstractProcessor {
    private final RoundAction roundAction;
    private final SourceVersion supportedSourceVersion;
    private final Set<String> supportedAnnotationTypeNames;

    public RoundActionProcessor(RoundAction roundAction, SourceVersion supportedSourceVersion, Set<String> supportedAnnotationTypeNames) {
        this.roundAction = roundAction;
        this.supportedSourceVersion = supportedSourceVersion;
        this.supportedAnnotationTypeNames = supportedAnnotationTypeNames;
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
