package com.dhemery.aggregator;

import com.dhemery.aggregator.internal.AggregateWriter;
import com.dhemery.aggregator.internal.Round;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Aggregates specially annotated methods
 * into aggregate classes during compilation.
 * <p>
 * An <strong>aggregate annotation</strong>
 * is an annotation marked by the {@link Aggregate} meta-annotation.
 * <p>
 * For each aggregate annotation,
 * this processor generates a Java source file.
 * The class is named and documented as specified by its {@code Aggregate} meta-annotation.
 * <p>
 * For each {@code public static} method marked by a given aggregate annotation,
 * the generated source file includes a method that:
 * <ul>
 * <li>Has the same signature as the annotated method.
 * <li>Has the same Javadoc comment as the annotated method.
 * <li>Calls the annotated method.
 * <li>Returns the annotated method's result.
 * </ul>
 */
public class Aggregator extends AbstractProcessor {
    public static Elements elements;
    private final Class<?>[] supportedAnnotationTypes = { Aggregate.class };

    public Aggregator(){}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment environment) {
        try {
            round(environment).aggregates().forEach(this::write);
        } catch (Throwable cause) {
            StringWriter trace = new StringWriter();
            PrintWriter out = new PrintWriter(trace);
            cause.printStackTrace(out);
            out.close();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Threw: " + cause + "\n" + cause.getMessage() + "\n" + trace.toString());
            throw cause;
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Arrays.stream(supportedAnnotationTypes).map(Class::getName).collect(toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    private Round round(RoundEnvironment environment) {
        return new Round(environment);
    }

    private void write(AggregateWriter aggregateWriter) {
        aggregateWriter.write(processingEnv.getFiler());
    }
}
