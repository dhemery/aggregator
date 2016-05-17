package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.Aggregate;
import com.dhemery.aggregator.AggregatorException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Writes a class that aggregates methods annotated a given aggregate annotation.
 */
public class AggregateWriter {
    private final TypeElement aggregateAnnotation;
    private final Collection<ExecutableElement> methods;
    private final TypeNamer namer;

    AggregateWriter(TypeElement aggregateAnnotation, Collection<ExecutableElement> methods, TypeNamer namer) {
        this.aggregateAnnotation = aggregateAnnotation;
        this.methods = methods;
        this.namer = namer;
    }

    public void write(Filer filer) {
        MethodWriter methodWriter = new MethodWriter(namer);

        PrintWriter out = printWriter(filer);
        out
                .format("package %s;%n", aggregatePackageName())
                .format("%n%s%n", imports())
                .format("%n%s", comment())
                .format("%s%n", generator())
                .format("public class %s {", aggregateSimpleName());
        methods
                .forEach(m -> m.accept(methodWriter, out::append));
        out
                .format("}")
                .close();
    }

    private String imports() {
        return namer.fullNames().stream()
                       .filter(i -> !Objects.equals(i, Object.class.getName()))
                       .sorted()
                       .map(i -> format("import %s;", i))
                       .collect(joining(System.lineSeparator()));
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
