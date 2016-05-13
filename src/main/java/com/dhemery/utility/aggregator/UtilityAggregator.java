package com.dhemery.utility.aggregator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * Aggregates specially annotated utility methods
 * into utility classes during compilation.
 * <p>
 * A <strong>utility annotation</strong>
 * is an annotation annotated by
 * the {@link SpecifiesAggregatedUtilityClass} meta-annotation.
 * <p>
 * For each utility annotation,
 * this processor generates a Java source file.
 * The class is named and documented as specified by the utility annotation.
 * <p>
 * For each {@code public static} method marked by a given utility annotation,
 * the generated source file includes a method that:
 * <ul>
 * <li>Has the same signature as the annotated method.
 * <li>Has the same Javadoc comment as the annotated method.
 * <li>Calls the annotated method.
 * <li>Returns the annotated method's result.
 * </ul>
 */
public class UtilityAggregator extends AbstractProcessor {
    private final Class<?>[] supportedAnnotationTypes = { SpecifiesAggregatedUtilityClass.class };
    private Filer filer;
    private Elements elements;

    public UtilityAggregator(){}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment environment) {
        round(environment).utilityClasses().forEach(c -> c.write(filer));
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
        return new Round(environment, elements);
    }
}
