package com.dhemery.annotation.testing;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@FunctionalInterface
public interface RoundAction {
    boolean process(Set<? extends TypeElement> annotations,
                    RoundEnvironment roundEnvironment,
                    ProcessingEnvironment processingEnvironment);
}
