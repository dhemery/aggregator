package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Processors {
    public static Processors.Builder processor() {
        return new Builder();
    }

    public static class Builder {
        private RoundProcessor roundProcessor = (annotations, roundEnv, processingEnv) -> false;
        private Set<String> supportedAnnotationTypes = new HashSet<>();
        private SourceVersion supportedSourceVersion = SourceVersion.RELEASE_8;

        public Builder onEachRound(RoundProcessor roundProcessor) {
            this.roundProcessor = roundProcessor;
            return this;
        }

        public Processor build() {
            return new ComposedProcessor(roundProcessor, supportedSourceVersion, supportedAnnotationTypes);
        }

        public Builder supportingAnnotationTypes(String... fullyQualifiedTypeNames) {
            Stream.of(fullyQualifiedTypeNames)
                    .forEach(supportedAnnotationTypes::add);
            return this;
        }

        public Builder supportingAnnotationTypes(Class<?>... types) {
            Stream.of(types)
                    .map(Class::getCanonicalName)
                    .forEach(supportedAnnotationTypes::add);
            return this;
        }
    }
}
