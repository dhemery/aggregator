package com.dhemery.utility.aggregator;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

/**
 * Represents the information supplied by the Java compiler
 * about a round of annotation processing.
 */
class Round {
    private final RoundEnvironment roundEnvironment;

    Round(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    Stream<UtilityClass> utilityClasses() {
        return roundEnvironment.getElementsAnnotatedWith(Aggregate.class).stream()
                       .map(TypeElement.class::cast)
                       .map(a -> new UtilityClass(a, this));
    }

    Stream<? extends Element> elementsAnnotatedWith(TypeElement utilityAnnotation) {
        return roundEnvironment.getElementsAnnotatedWith(utilityAnnotation).stream();
    }
}
