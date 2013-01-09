package com.dhemery.factory;

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

    public Iterable<FactoryClass> factoryClasses() {
        Set<FactoryClass> factoryClasses = new HashSet<FactoryClass>();
        for(TypeElement factoryAnnotation : factoryAnnotationsIn(elementsAnnotatedAs(FactoryAnnotation.class))) {
            factoryClasses.add(factoryClass(factoryAnnotation));
        }
        return factoryClasses;
    }

    private FactoryClass factoryClass(TypeElement factoryAnnotation) {
        return new FactoryClass(factoryAnnotation, factoryMethodsAnnotatedAs(factoryAnnotation));
    }

    private Collection<FactoryMethod> factoryMethodsAnnotatedAs(TypeElement factoryAnnotation) {
        return factoryMethodsIn(elementsAnnotatedAs(factoryAnnotation));
    }

    private Collection<FactoryMethod> factoryMethodsIn(Set<? extends Element> elements) {
        Set<FactoryMethod> factoryMethods = new HashSet<FactoryMethod>();
        for(Element element : elements) {
            factoryMethods.add(factoryMethod(element));
        }
        return factoryMethods;
    }

    private FactoryMethod factoryMethod(Element element) {
        return new FactoryMethod((ExecutableElement) element, processingEnvironment);
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
