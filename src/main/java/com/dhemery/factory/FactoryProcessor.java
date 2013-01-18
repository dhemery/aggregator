package com.dhemery.factory;

import com.dhemery.factory.internal.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * An annotation processor
 * that generates factory class source files
 * based on specially annotated factory methods.
 *
 * <p>A <strong>factory annotation</strong>
 * is an annotation that is itself annotated with {@link Factory}.
 *
 * <p>For each factory annotation,
 * this processor generates a source file for a Java class.
 * The class is named and documented as specified by the factory annotation.
 *
 * <p>For each factory method marked by the factory annotation,
 * the generated class file includes a method that:
 * <ul>
 * <li>Has the same signature as the annotated factory method.
 * <li>Has the same Javadoc comment as the annotated factory method.
 * <li>Calls the annotated factory method.
 * <li>Returns the annotated factory method's result.
 * </ul>
 */
@SupportedAnnotationTypes("com.dhemery.factory.Factory")
public class FactoryProcessor extends AbstractProcessor {
    private Comparator<String> typeNameComparator = new TypeNameComparator();
    private Comparator<Element> parameterTypeComparator = new ParameterTypeComparator(typeNameComparator);
    private Comparator<List<? extends Element>> parameterListComparator = new ListComparator<>(parameterTypeComparator);
    private Comparator<FactoryMethod> methodComparator = new FactoryMethodComparator(parameterListComparator);

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment environment) {
        FactoryClassWriter classWriter = classWriter(filer(), methodWriter());
        for(FactoryClass c : factoryClassesIn(environment)) {
            classWriter.write(c);
        }
        return true;
    }

    private FactoryClassWriter classWriter(Filer filer, FactoryMethodWriter methodWriter) {
        return new FactoryClassWriter(filer, methodWriter, methodComparator);
    }

    private Filer filer() {
        return processingEnv.getFiler();
    }

    private FactoryMethodWriter methodWriter() {
        return new FactoryMethodWriter();
    }

    private Iterable<FactoryClass> factoryClassesIn(RoundEnvironment roundEnvironment) {
        Round round = new Round(roundEnvironment, processingEnv);
        return round.factoryClasses();
    }
}
