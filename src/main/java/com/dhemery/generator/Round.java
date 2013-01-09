package com.dhemery.generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Round {
    private final RoundEnvironment roundEnvironment;
    private final ProcessingEnvironment processingEnvironment;

    public Round(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        this.roundEnvironment = roundEnvironment;
        this.processingEnvironment = processingEnvironment;
    }

    public Iterable<UtilityClassWriter> factoryClasses() {
        Set<UtilityClassWriter> factoryClasses = new HashSet<UtilityClassWriter>();
        for(TypeElement factoryAnnotation : factoryAnnotationsIn(elementsAnnotatedAs(Generate.class))) {
            factoryClasses.add(factoryClass(factoryAnnotation));
        }
        return factoryClasses;
    }

    private UtilityClassWriter factoryClass(TypeElement factoryAnnotation) {
        return new UtilityClassWriter(factoryAnnotation, factoryMethodsAnnotatedAs(factoryAnnotation));
    }

    private Collection<UtilityMethodWriter> factoryMethodsAnnotatedAs(TypeElement factoryAnnotation) {
        return factoryMethodsIn(elementsAnnotatedAs(factoryAnnotation));
    }

    private Collection<UtilityMethodWriter> factoryMethodsIn(Set<? extends Element> elements) {
        Set<UtilityMethodWriter> factoryMethods = new HashSet<UtilityMethodWriter>();
        for(Element element : elements) {
            factoryMethods.add(factoryMethod(element));
        }
        return factoryMethods;
    }

    private UtilityMethodWriter factoryMethod(Element element) {
        return new UtilityMethodWriter((ExecutableElement) element, processingEnvironment);
    }

    private static Iterable<TypeElement> factoryAnnotationsIn(Set<? extends Element> elements) {
        Set<TypeElement> factoryAnnotations = new HashSet<TypeElement>();
        for(Element element : elements) {
            factoryAnnotations.add((TypeElement) element);
        }
        return factoryAnnotations;
    }

    private Set<? extends Element> elementsAnnotatedAs(Class<? extends Annotation> annotation) {
        return roundEnvironment.getElementsAnnotatedWith(annotation);
    }

    private Set<? extends Element> elementsAnnotatedAs(TypeElement annotationElement) {
        return roundEnvironment.getElementsAnnotatedWith(annotationElement);
    }
}
