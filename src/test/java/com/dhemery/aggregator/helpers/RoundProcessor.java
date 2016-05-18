package com.dhemery.aggregator.helpers;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@FunctionalInterface
public interface RoundProcessor {
    boolean processRound(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnvironment,
                         ProcessingEnvironment processingEnvironment);
}
