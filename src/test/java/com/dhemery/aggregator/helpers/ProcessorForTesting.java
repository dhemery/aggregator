package com.dhemery.aggregator.helpers;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ProcessorForTesting extends AbstractProcessor implements TestProcessor {
    private final Consumer<TestProcessor> test;
    private Set<? extends TypeElement> annotations;
    private RoundEnvironment roundEnvironment;

    public ProcessorForTesting(Consumer<TestProcessor> test) {
        this.test = test;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        this.annotations = annotations;
        this.roundEnvironment = roundEnvironment;
        test.accept(this);
        return false;
    }

    @Override
    public Set<? extends TypeElement> annotations() {
        return Collections.unmodifiableSet(annotations);
    }

    @Override
    public ProcessingEnvironment processingEnvironment() {
        return processingEnv;
    }

    @Override
    public RoundEnvironment roundEnvironment() {
        return roundEnvironment;
    }
}
