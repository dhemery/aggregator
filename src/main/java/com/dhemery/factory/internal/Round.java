package com.dhemery.factory.internal;

import com.dhemery.factory.Factory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Represents the information supplied by the Java compiler
 * about a round of annotation processing.
 */
public class Round {
    private static final List<Modifier> FACTORY_METHOD_REQUIRED_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final RoundEnvironment roundEnvironment;
    private final ProcessingEnvironment processingEnvironment;

    public Round(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        this.roundEnvironment = roundEnvironment;
        this.processingEnvironment = processingEnvironment;
    }

    public Iterable<FactoryClass> factoryClasses() {
        Set<FactoryClass> factoryClasses = new HashSet<>();
        for(TypeElement generatorAnnotation : generatorAnnotations()) {
            factoryClasses.add(factoryClassFor(generatorAnnotation));
        }
        return factoryClasses;
    }

    private Set<? extends Element> elementsAnnotatedWith(Class<? extends Annotation> annotation) {
        return roundEnvironment.getElementsAnnotatedWith(annotation);
    }

    private Set<? extends Element> elementsAnnotatedWith(TypeElement annotationElement) {
        return roundEnvironment.getElementsAnnotatedWith(annotationElement);
    }

    private Iterable<TypeElement> generatorAnnotations() {
        return generatorAnnotationsIn(elementsAnnotatedWith(Factory.class));
    }

    private Iterable<TypeElement> generatorAnnotationsIn(Set<? extends Element> elements) {
        Set<TypeElement> generatorAnnotations = new HashSet<>();
        for(Element element : elements) {
            if(isFactoryAnnotation(element)) {
                generatorAnnotations.add((TypeElement) element);
            }
        }
        return generatorAnnotations;
    }

    private void error(Element element, String message) {
        String fullMessage = element.toString() + ' ' + message;
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, fullMessage);
    }

    private FactoryClass factoryClassFor(TypeElement factoryAnnotation) {
        return new FactoryClass(factoryAnnotation, factoryMethodsAnnotatedWith(factoryAnnotation));
    }

    private FactoryMethod factoryyMethod(Element element) {
        return new FactoryMethod((ExecutableElement) element, processingEnvironment);
    }

    private Collection<FactoryMethod> factoryMethodsAnnotatedWith(TypeElement factoryAnnotation) {
        return factoryMethodsIn(elementsAnnotatedWith(factoryAnnotation));
    }

    private Collection<FactoryMethod> factoryMethodsIn(Set<? extends Element> elements) {
        Set<FactoryMethod> factoryMethods = new HashSet<>();
        for(Element element : elements) {
            if(isFactoryMethod(element)) {
                factoryMethods.add(factoryyMethod(element));
            } else {
                // Should emit some warning here.
            }
        }
        return factoryMethods;
    }

    private static boolean isFactoryAnnotation(Element ignored) {
        // todo: Check that the annotation applies only to methods.  What else?
        return true;
    }

    private boolean isFactoryMethod(Element element) {
        // todo: Check that the return type is not void.
        return element.getModifiers().containsAll(FACTORY_METHOD_REQUIRED_MODIFIERS);
    }
}
