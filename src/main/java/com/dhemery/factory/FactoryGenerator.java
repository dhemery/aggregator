package com.dhemery.factory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.dhemery.factory.FactoryAnnotation")
public class FactoryGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotationTypes, RoundEnvironment roundEnvironment) {
        Filer filer = processingEnv.getFiler();
        Round round = new Round(roundEnvironment, processingEnv);
        for(FactoryClass factoryClass : round.factoryClasses()) {
            factoryClass.write(filer);
        }
        return true;
    }
}
