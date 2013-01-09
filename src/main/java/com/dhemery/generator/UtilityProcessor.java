package com.dhemery.generator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.dhemery.generator.Generate")
public class UtilityProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment roundEnvironment) {
        Filer filer = processingEnv.getFiler();
        Round round = new Round(roundEnvironment, processingEnv);
        for(UtilityClassWriter factoryClass : round.factoryClasses()) {
            factoryClass.write(filer);
        }
        return true;
    }
}
