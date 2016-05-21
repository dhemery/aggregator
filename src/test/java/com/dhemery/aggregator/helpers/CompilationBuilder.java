package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.dhemery.aggregator.helpers.ProcessorBuilder.processor;
import static com.dhemery.aggregator.helpers.ProjectBuilder.project;
import static com.dhemery.aggregator.helpers.Streams.streamOf;

public class CompilationBuilder {
    private final Set<SourceFile> sourceFiles = new HashSet<>();
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();

    private CompilationBuilder() {
    }

    public static WithAnnotations process(String annotationTypeName, String... others) {
        return new CompilationBuilder().new WithAnnotations().and(annotationTypeName, others);
    }

    @SafeVarargs
    public static WithAnnotations process(Class<? extends Annotation> annotationType, Class<? extends Annotation>... others) {
        return new CompilationBuilder().new WithAnnotations().and(annotationType, others);
    }

    public class WithAnnotations {
        private WithAnnotations() {
        }

        public WithAnnotations and(String annotationTypeName, String... others) {
            streamOf(annotationTypeName, others).forEach(supportedAnnotationTypeNames::add);
            return this;
        }

        public WithAnnotations and(Class<? extends Annotation> annotationType, Class<? extends Annotation>... others) {
            streamOf(annotationType, others).map(Class::getCanonicalName).forEach(supportedAnnotationTypeNames::add);
            return this;
        }

        public WithSourceFiles in(SourceFile sourceFile, SourceFile... others) {
            return new WithSourceFiles().and(sourceFile, others);
        }
    }

    public class WithSourceFiles {
        private WithSourceFiles() {
        }

        public WithSourceFiles and(SourceFile sourceFile, SourceFile... others) {
            streamOf(sourceFile, others).forEach(CompilationBuilder.this.sourceFiles::add);
            return this;
        }

        public boolean by(RoundAction action) {
            Processor processor = processor()
                                          .supporting(supportedAnnotationTypeNames)
                                          .onEachRound(action);
            return project()
                           .with(sourceFiles)
                           .compileWith(processor);
        }

        public boolean by(Consumer<RoundEnvironment> consumer) {
            return by((annotations, roundEnvironment, processingEnvironment) -> {
                consumer.accept(roundEnvironment);
                return false;
            });
        }
    }
}
