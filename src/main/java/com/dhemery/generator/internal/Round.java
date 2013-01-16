package com.dhemery.generator.internal;

import com.dhemery.generator.Generate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.*;

public class Round {
    private static final List<Modifier> UTILITY_METHOD_REQUIRED_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final RoundEnvironment roundEnvironment;
    private final ProcessingEnvironment processingEnvironment;

    public Round(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        this.roundEnvironment = roundEnvironment;
        this.processingEnvironment = processingEnvironment;
    }

    public Iterable<UtilityClass> utilityClasses() {
        Set<UtilityClass> utilityClasses = new HashSet<UtilityClass>();
        for(TypeElement generatorAnnotation : generatorAnnotations()) {
            utilityClasses.add(utilityClassFor(generatorAnnotation));
        }
        return utilityClasses;
    }

    private Set<? extends Element> elementsAnnotatedWith(Class<? extends Annotation> annotation) {
        return roundEnvironment.getElementsAnnotatedWith(annotation);
    }

    private Set<? extends Element> elementsAnnotatedWith(TypeElement annotationElement) {
        return roundEnvironment.getElementsAnnotatedWith(annotationElement);
    }

    private Iterable<TypeElement> generatorAnnotations() {
        return generatorAnnotationsIn(elementsAnnotatedWith(Generate.class));
    }

    private Iterable<TypeElement> generatorAnnotationsIn(Set<? extends Element> elements) {
        Set<TypeElement> generatorAnnotations = new HashSet<TypeElement>();
        for(Element element : elements) {
            if(isGeneratorAnnotation(element)) {
                generatorAnnotations.add((TypeElement) element);
            }
        }
        return generatorAnnotations;
    }

    private void error(Element element, String message) {
        String fullMessage = element.toString() + ' ' + message;
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, fullMessage);
    }

    private static boolean isGeneratorAnnotation(Element element) {
        return true;
    }

    private boolean isUtilityMethod(Element element) {
        return element.getModifiers().containsAll(UTILITY_METHOD_REQUIRED_MODIFIERS);
    }

    private UtilityClass utilityClassFor(TypeElement factoryAnnotation) {
        return new UtilityClass(factoryAnnotation, utilityMethodsAnnotatedWith(factoryAnnotation));
    }

    private UtilityMethod utilityyMethod(Element element) {
        return new UtilityMethod((ExecutableElement) element, processingEnvironment);
    }

    private Collection<UtilityMethod> utilityMethodsAnnotatedWith(TypeElement factoryAnnotation) {
        return utilityMethodsIn(elementsAnnotatedWith(factoryAnnotation));
    }

    private Collection<UtilityMethod> utilityMethodsIn(Set<? extends Element> elements) {
        Set<UtilityMethod> utilityMethods = new HashSet<UtilityMethod>();
        for(Element element : elements) {
            if(isUtilityMethod(element)) {
                utilityMethods.add(utilityyMethod(element));
            } else {
//                error(element, "must be declared public static");
            }
        }
        return utilityMethods;
    }
}
