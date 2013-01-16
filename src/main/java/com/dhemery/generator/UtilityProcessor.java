package com.dhemery.generator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.dhemery.generator.Generate")
public class UtilityProcessor extends AbstractProcessor {
    private Comparator<String> typeNameComparator = new TypeNameComparator();
    private Comparator<Element> parameterTypeComparator = new ParameterTypeComparator(typeNameComparator);
    private Comparator<List<? extends Element>> parameterListComparator = new ListComparator<>(parameterTypeComparator);
    private Comparator<UtilityMethod> methodComparator = new UtilityMethodComparator(parameterListComparator);

    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment environment) {
        UtilityClassWriter classWriter = classWriter(filer(), methodWriter());
        for(UtilityClass c : utilityClassesIn(environment)) {
            classWriter.write(c);
        }
        return true;
    }

    private UtilityClassWriter classWriter(Filer filer, UtilityMethodWriter methodWriter) {
        return new UtilityClassWriter(filer, methodWriter, methodComparator);
    }

    private Filer filer() {
        return processingEnv.getFiler();
    }

    private UtilityMethodWriter methodWriter() {
        return new UtilityMethodWriter();
    }

    private Iterable<UtilityClass> utilityClassesIn(RoundEnvironment roundEnvironment) {
        Round round = new Round(roundEnvironment, processingEnv);
        return round.utilityClasses();
    }
}
