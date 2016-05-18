package com.dhemery.aggregator.helpers;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class ComposedProcessor extends AbstractProcessor {
    private final RoundProcessor roundAction;
    private final SourceVersion supportedSourceVersion;
    private final Set<String> supportedAnnotationTypes;

    public ComposedProcessor(RoundProcessor roundAction, SourceVersion supportedSourceVersion, Set<String> supportedAnnotationTypes) {
        this.roundAction = roundAction;
        this.supportedSourceVersion = supportedSourceVersion;
        this.supportedAnnotationTypes = supportedAnnotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return roundAction.processRound(annotations, roundEnv, processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return supportedSourceVersion;
    }
}
