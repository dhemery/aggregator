package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.Aggregate;
import com.dhemery.aggregator.AggregatorException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Writes a class that aggregates methods annotated a given aggregate annotation.
 */
public class AggregateWriter {
    private static final List<Modifier> UTILITY_METHOD_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final TypeElement aggregateAnnotation;
    private final Round round;
    private final TypeSpy spy;

    AggregateWriter(TypeElement aggregateAnnotation, Round round, TypeSpy spy) {
        this.aggregateAnnotation = aggregateAnnotation;
        this.round = round;
        this.spy = spy;
    }

    public void write(Filer filer) {
        Set<String> types = new HashSet<>();
        methods()
                .map(Element::asType)
                .forEach(m -> m.accept(spy, types::add));
        SimplifyingTypeNamer namer = new SimplifyingTypeNamer(types);
        MethodWriter methodWriter = new MethodWriter(namer);

        PrintWriter out = printWriter(filer);
        out
                .format("package %s;%n%n", aggregatePackageName())
                .append(namer.imports())
                .format("%n%s", comment())
                .format("%s%n", generator())
                .format("public class %s {", aggregateSimpleName());
        methods()
                .forEach(m -> m.accept(methodWriter, out::append));
        out
                .format("}")
                .close();
    }

    private Aggregate aggregate() {
        return aggregateAnnotation.getAnnotation(Aggregate.class);
    }

    private String aggregateAnnotationName() {
        return aggregateAnnotation.getQualifiedName().toString();
    }

    private String aggregateName() {
        return aggregate().className();
    }

    private String aggregatePackageName() {
        return packageName(aggregateName());
    }

    private String aggregateSimpleName() {
        return SimplifyingTypeNamer.simpleName(aggregateName());
    }

    private String comment() {
        return Comment.forClass(aggregate().classComment());
    }

    private String generator() {
        return format(
                "@javax.annotation.Generated(value=\"%s\", comments=\"%s\", date=\"%s\")",
                generatorName(),
                generatorComment(),
                new Date());
    }

    private String generatorComment() {
        return format("Aggregation of methods annotated with %s", aggregateAnnotationName());
    }

    private String generatorName() {
        return Aggregate.class.getName();
    }

    private Stream<ExecutableElement> methods() {
        return round.elementsAnnotatedWith(aggregateAnnotation)
                       .filter(annotatedElement -> annotatedElement.getKind() == ElementKind.METHOD)
                       .filter(methodElement -> methodElement.getModifiers().containsAll(UTILITY_METHOD_MODIFIERS))
                       .map(ExecutableElement.class::cast);
    }

    private String packageName(String qualifiedName) {
        return qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
    }

    private PrintWriter printWriter(Filer filer) {
        String aggregateName = aggregateName();
        try {
            JavaFileObject sourceFile = filer.createSourceFile(aggregateName);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new AggregatorException(aggregateName(), aggregateAnnotationName(), cause);
        }
    }
}
