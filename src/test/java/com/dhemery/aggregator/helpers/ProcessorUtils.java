package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.*;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processorSupporting;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;

public class ProcessorUtils {
    public static final Predicate<Object> ANY = o -> true;
    public static final Function<Element, TypeMirror> ELEMENT_TYPE = Element::asType;
    public static final Function<? super Element, TypeMirror> RETURN_TYPE = e -> ExecutableElement.class.cast(e).getReturnType();
    public static final Function<RoundEnvironment, Set<? extends Element>> ROOT_ELEMENTS = RoundEnvironment::getRootElements;

    public static boolean processProject(SourceFile sourceFile,
                                         String supportedAnnotationTypeName,
                                         Function<RoundEnvironment, Set<? extends Element>> elementFinder,
                                         Predicate<? super Element> elementFilter,
                                         Function<? super Element, ? extends TypeMirror> typeMapper,
                                         Consumer<? super TypeMirror> typeConsumer) {
        RoundAction roundAction = roundAction(elementFinder, elementFilter, typeMapper, typeConsumer);
        Processor processor = processorSupporting(supportedAnnotationTypeName).onEachRound(roundAction);
        return project().with(sourceFile).compileWith(processor);
    }

    public static boolean eachRoot(SourceFile sourceFile,
                                   Predicate<? super Element> elementFilter,
                                   Function<? super Element, ? extends TypeMirror> typeMapper,
                                   Consumer<? super TypeMirror> typeConsumer) {
        return processProject(sourceFile, "*", ROOT_ELEMENTS, elementFilter, typeMapper, typeConsumer);
    }

    public static boolean eachRoot(SourceFile sourceFile,
                                   Function<? super Element, ? extends TypeMirror> typeMapper,
                                   Consumer<? super TypeMirror> typeConsumer) {
        return eachRoot(sourceFile, ANY, typeMapper, typeConsumer);
    }

    public static boolean eachRoot(SourceFile sourceFile,
                                   Predicate<? super Element> elementFilter,
                                   Consumer<? super TypeMirror> typeConsumer) {
        return eachRoot(sourceFile, elementFilter, ELEMENT_TYPE, typeConsumer);
    }

    public static boolean eachRoot(SourceFile sourceFile,
                                   Consumer<? super TypeMirror> typeConsumer) {
        return eachRoot(sourceFile, ANY, ELEMENT_TYPE, typeConsumer);
    }

    public static boolean each(SourceFile sourceFile,
                               Class<? extends Annotation> targetAnnotationType,
                               Predicate<? super Element> elementFilter,
                               Function<? super Element, ? extends TypeMirror> typeMapper,
                               Consumer<? super TypeMirror> typeConsumer) {
        return processProject(sourceFile, targetAnnotationType.getCanonicalName(), elementsAnnotatedWith(targetAnnotationType), elementFilter, typeMapper, typeConsumer);
    }

    public static boolean each(SourceFile sourceFile,
                               Class<? extends Annotation> targetAnnotationType,
                               Function<? super Element, ? extends TypeMirror> typeMapper,
                               Consumer<? super TypeMirror> typeConsumer) {
        return each(sourceFile, targetAnnotationType, ANY, typeMapper, typeConsumer);
    }

    public static boolean each(SourceFile sourceFile,
                               Class<? extends Annotation> targetAnnotationType,
                               Predicate<? super Element> elementFilter,
                               Consumer<? super TypeMirror> typeConsumer) {
        return each(sourceFile, targetAnnotationType, elementFilter, ELEMENT_TYPE, typeConsumer);
    }

    public static boolean each(SourceFile sourceFile,
                               Class<? extends Annotation> targetAnnotationType,
                               Consumer<? super TypeMirror> typeConsumer) {
        return each(sourceFile, targetAnnotationType, ANY, ELEMENT_TYPE, typeConsumer);
    }

    public static Function<RoundEnvironment, Set<? extends Element>> elementsAnnotatedWith(Class<? extends Annotation> targetAnnotationType) {
        return re -> re.getElementsAnnotatedWith(targetAnnotationType);
    }

    public static RoundAction roundAction(Function<RoundEnvironment, Set<? extends Element>> elementFinder,
                                          Predicate<? super Element> elementFilter,
                                          Function<? super Element, ? extends TypeMirror> typeMapper,
                                          Consumer<? super TypeMirror> typeConsumer) {
        return (a, r, p) -> {
            elementFinder.apply(r).stream()
                    .filter(elementFilter)
                    .map(typeMapper)
                    .forEach(typeConsumer);
            return false;
        };
    }
}
