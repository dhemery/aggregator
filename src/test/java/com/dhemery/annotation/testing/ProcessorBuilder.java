package com.dhemery.annotation.testing;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;

import static com.dhemery.annotation.testing.RoundAction.onRoundEnvironment;

public class ProcessorBuilder {
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();
    private SourceVersion supportedSourceVersion = SourceVersion.latestSupported();

    private ProcessorBuilder() {
    }

    public static Fresh processor() {
        return new ProcessorBuilder().new Fresh();
    }

    public class Fresh extends WithSourceVersion {
        public WithSourceVersion forSourceVersion(SourceVersion sourceVersion) {
            supportedSourceVersion = sourceVersion;
            return new WithSourceVersion();
        }
    }

    public class WithSourceVersion {
        public WithSupportedTypes supportingAnnotationType(Class<? extends Annotation> type) {
            return new WithSupportedTypes().andAnnotationType(type);
        }

        public WithSupportedTypes supportingAnnotationTypes(Class<? extends Annotation> type, Class<? extends Annotation> another, Class<? extends Annotation>... others) {
            return new WithSupportedTypes().andAnnotationTypes(type, another, others);
        }

        public WithSupportedTypes supportingAnnotationTypes(Collection<Class<? extends Annotation>> types) {
            return new WithSupportedTypes().andAnnotationTypes(types);
        }

        public WithSupportedTypes supportingAnnotationTypeNamed(String name) {
            return new WithSupportedTypes().andAnnotationTypeNamed(name);
        }

        public WithSupportedTypes supportingAnnotationTypesNamed(String name, String another, String... others) {
            return new WithSupportedTypes().andAnnotationTypeNamed(name, another, others);
        }

        public WithSupportedTypes supportingAnnotationTypesNamed(Collection<String> names) {
            return new WithSupportedTypes().andAnnotationTypesNamed(names);
        }
    }

    public class WithSupportedTypes {
        private WithSupportedTypes() {
        }

        public WithSupportedTypes andAnnotationType(Class<? extends Annotation> type) {
            supportedAnnotationTypeNames.add(type.getCanonicalName());
            return this;
        }

        public WithSupportedTypes andAnnotationTypes(Class<? extends Annotation> type, Class<? extends Annotation> another, Class<? extends Annotation>[] others) {
            return andAnnotationType(type)
                           .andAnnotationType(another)
                           .andAnnotationTypes(Arrays.asList(others));
        }

        public WithSupportedTypes andAnnotationTypes(Collection<Class<? extends Annotation>> types) {
            types.stream()
                    .forEach(this::andAnnotationType);
            return this;
        }

        public WithSupportedTypes andAnnotationTypeNamed(String name) {
            supportedAnnotationTypeNames.add(name);
            return this;
        }

        public WithSupportedTypes andAnnotationTypeNamed(String name, String another, String... others) {
            return andAnnotationTypeNamed(name)
                           .andAnnotationTypeNamed(another)
                           .andAnnotationTypesNamed(Arrays.asList(others));
        }

        public WithSupportedTypes andAnnotationTypesNamed(Collection<String> names) {
            supportedAnnotationTypeNames.addAll(names);
            return this;
        }

        public Processor performingOnEachRound(RoundAction action) {
            return new RoundActionProcessor(action, supportedSourceVersion, supportedAnnotationTypeNames);
        }

        public Processor performingOnEachRound(Consumer<? super RoundEnvironment> consumer) {
            return performingOnEachRound(onRoundEnvironment(consumer));
        }
    }
}
