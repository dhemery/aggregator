package com.dhemery.utility.aggregator;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

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
        return roundEnvironment.getElementsAnnotatedWith(SpecifiesAggregatedUtilityClass.class).stream()
                       .map(TypeElement.class::cast)
                       .map(a -> new UtilityClass(a, this));
    }

    Stream<? extends Element> elementsAnnotatedWith(TypeElement utilityAnnotation) {
        return roundEnvironment.getElementsAnnotatedWith(utilityAnnotation).stream();
    }
}
