package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.Aggregate;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

/**
 * Represents the information supplied by the Java compiler
 * about a round of annotation processing.
 */
public class Round {
    private final RoundEnvironment roundEnvironment;
    private final TypeSpy typeSpy = new TypeSpy();

    public Round(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    public Stream<AggregateWriter> aggregates() {
        return roundEnvironment.getElementsAnnotatedWith(Aggregate.class).stream()
                       .map(TypeElement.class::cast)
                       .map(a -> new AggregateWriter(a, this, typeSpy));
    }

    Stream<? extends Element> elementsAnnotatedWith(TypeElement aggregateAnnotation) {
        return roundEnvironment.getElementsAnnotatedWith(aggregateAnnotation).stream();
    }
}
