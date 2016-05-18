package com.dhemery.aggregator.helpers;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public interface TestProcessor {
    Set<? extends TypeElement> annotations();

    ProcessingEnvironment processingEnvironment();

    RoundEnvironment roundEnvironment();
}
