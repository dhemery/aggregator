package com.dhemery.generator;

import com.dhemery.generator.internal.Round;
import com.dhemery.generator.internal.UtilityClass;
import com.dhemery.generator.internal.UtilityClassWriter;
import com.dhemery.generator.internal.UtilityMethodWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.dhemery.generator.Generate")
public class UtilityProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment environment) {
        UtilityClassWriter classWriter = classWriter(filer(), methodWriter());
        for(UtilityClass c : utilityClassesIn(round(environment))) {
            classWriter.write(c);
        }
        return true;
    }

    private UtilityClassWriter classWriter(Filer filer, UtilityMethodWriter methodWriter) {
        return new UtilityClassWriter(filer, methodWriter);
    }

    private Filer filer() {
        return processingEnv.getFiler();
    }

    private UtilityMethodWriter methodWriter() {
        return new UtilityMethodWriter();
    }

    private Round round(RoundEnvironment roundEnvironment) {
        return new Round(roundEnvironment, processingEnv);
    }

    private Iterable<UtilityClass> utilityClassesIn(Round round) {
        return round.utilityClasses();
    }
}
