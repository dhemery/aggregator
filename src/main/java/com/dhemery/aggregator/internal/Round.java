package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.Aggregate;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Represents the information supplied by the Java compiler
 * about a round of annotation processing.
 */
public class Round {
    private static final List<Modifier> UTILITY_METHOD_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final RoundEnvironment roundEnvironment;
    private final TypeSpy typeSpy = new TypeSpy();

    public Round(RoundEnvironment roundEnvironment) {
        this.roundEnvironment = roundEnvironment;
    }

    public Stream<AggregateWriter> aggregates() {
        return roundEnvironment.getElementsAnnotatedWith(Aggregate.class).stream()
                       .map(TypeElement.class::cast)
                       .map(a -> new AggregateWriter(a, methods(a), namer(methods(a))));
    }

    private Collection<ExecutableElement> methods(TypeElement a) {
        return elementsAnnotatedWith(a)
                       .filter(annotatedElement -> annotatedElement.getKind() == ElementKind.METHOD)
                       .filter(methodElement -> methodElement.getModifiers().containsAll(UTILITY_METHOD_MODIFIERS))
                       .map(ExecutableElement.class::cast)
                       .collect(toList());
    }

    private Stream<? extends Element> elementsAnnotatedWith(TypeElement aggregateAnnotation) {
        return roundEnvironment.getElementsAnnotatedWith(aggregateAnnotation).stream();
    }

    private SimplifyingTypeReferences namer(Collection<ExecutableElement> methods) {
        Set<String> types = new HashSet<>();
        methods.stream()
                .map(Element::asType)
                .forEach(m -> m.accept(typeSpy, types::add));
        return new SimplifyingTypeReferences(types);
    }
}
