package com.dhemery.utility.aggregator;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Represents the information supplied by the Java compiler
 * about a round of annotation processing.
 */
class Round {
    private static final List<Modifier> UTILITY_METHOD_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final RoundEnvironment roundEnvironment;
    private final Elements elements;

    Round(RoundEnvironment roundEnvironment, Elements elements) {
        this.roundEnvironment = roundEnvironment;
        this.elements = elements;
    }

    Set<UtilityClass> utilityClasses() {
        return roundEnvironment.getElementsAnnotatedWith(SpecifiesAggregatedUtilityClass.class).stream()
                       .map(TypeElement.class::cast)
                       .map(a -> new UtilityClass(a, methodsAnnotatedWith(a)))
                       .collect(toSet());
    }

    private Collection<UtilityMethod> methodsAnnotatedWith(TypeElement utilityAnnotation) {
        return roundEnvironment.getElementsAnnotatedWith(utilityAnnotation).stream()
                       .filter(annotatedElement -> annotatedElement.getKind() == ElementKind.METHOD)
                       .filter(methodElement -> methodElement.getModifiers().containsAll(UTILITY_METHOD_MODIFIERS))
                       .map(ExecutableElement.class::cast)
                       .map(e -> new UtilityMethod(e, elements))
                       .collect(toSet());
    }
}
