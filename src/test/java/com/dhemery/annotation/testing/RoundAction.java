package com.dhemery.annotation.testing;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.function.Consumer;

@FunctionalInterface
public interface RoundAction {
    boolean process(Set<? extends TypeElement> annotations,
                    RoundEnvironment roundEnvironment,
                    ProcessingEnvironment processingEnvironment);

    static RoundAction onRoundEnvironment(Consumer<? super RoundEnvironment> consumer) {
        return (annotations, roundEnvironment, processingEnvironment) -> {
            consumer.accept(roundEnvironment);
            return false;
        };
    }
}
