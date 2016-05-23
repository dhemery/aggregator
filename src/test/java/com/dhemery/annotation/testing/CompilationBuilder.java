package com.dhemery.annotation.testing;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;

import static com.dhemery.annotation.testing.ProcessorBuilder.processor;
import static com.dhemery.annotation.testing.ProjectBuilder.project;
import static com.dhemery.annotation.testing.RoundAction.onRoundEnvironment;

public class CompilationBuilder {
    private final Set<SourceFile> sourceFiles = new HashSet<>();
    private final Set<String> supportedAnnotationTypeNames = new HashSet<>();

    private CompilationBuilder() {
    }

    public static CompilationBuilder process() {
        return new CompilationBuilder();
    }

    public WithAnnotations annotationTypeNamed(String name) {
        return new WithAnnotations().andAnnotationTypeNamed(name);
    }

    public WithAnnotations annotationTypesNamed(String name, String another, String... others) {
        return new WithAnnotations().andAnnotationTypesNamed(name, another, others);
    }

    public WithAnnotations annotationTypesNamed(Collection<String> names) {
        return new WithAnnotations().andAnnotationTypesNamed(names);
    }

    public WithAnnotations annotationType(Class<? extends Annotation> type) {
        return new WithAnnotations().andAnnotationType(type);
    }

    public WithAnnotations annotationTypes(Class<? extends Annotation> type, Class<? extends Annotation> another, Class<? extends Annotation>... others) {
        return new WithAnnotations().andAnnotationTypes(type, another, others);
    }

    public WithAnnotations annotationType(Collection<Class<? extends Annotation>> types) {
        return new WithAnnotations().andAnnotationTypes(types);
    }

    public class WithAnnotations {
        private WithAnnotations() {
        }

        public WithAnnotations andAnnotationTypeNamed(String name) {
            supportedAnnotationTypeNames.add(name);
            return this;
        }

        public WithAnnotations andAnnotationTypesNamed(String name, String another, String... others) {
            return andAnnotationTypeNamed(name)
                           .andAnnotationTypeNamed(another)
                           .andAnnotationTypesNamed(Arrays.asList(others));
        }

        public WithAnnotations andAnnotationTypesNamed(Collection<String> names) {
            supportedAnnotationTypeNames.addAll(names);
            return this;
        }

        public WithAnnotations andAnnotationType(Class<? extends Annotation> type) {
            supportedAnnotationTypeNames.add(type.getCanonicalName());
            return this;
        }

        public WithAnnotations andAnnotationTypes(Class<? extends Annotation> type, Class<? extends Annotation> another, Class<? extends Annotation>... others) {
            return andAnnotationType(type)
                           .andAnnotationType(another)
                           .andAnnotationTypes(Arrays.asList(others));
        }

        public WithAnnotations andAnnotationTypes(Collection<Class<? extends Annotation>> types) {
            types.stream().forEach(this::andAnnotationType);
            return this;
        }

        public WithSourceFiles inSourceFile(SourceFile file) {
            return new WithSourceFiles().andSourceFile(file);
        }

        public WithSourceFiles inSourceFiles(SourceFile file, SourceFile another, SourceFile... others) {
            return new WithSourceFiles().andSourceFiles(file, another, others);
        }

        public WithSourceFiles inSourceFiles(Collection<SourceFile> files) {
            return new WithSourceFiles().andSourceFiles(files);
        }
    }

    public class WithSourceFiles {
        private WithSourceFiles() {
        }

        public WithSourceFiles andSourceFile(SourceFile file) {
            sourceFiles.add(file);
            return this;
        }

        public WithSourceFiles andSourceFiles(SourceFile file, SourceFile another, SourceFile... others) {
            return this.andSourceFile(file)
                           .andSourceFile(another)
                           .andSourceFiles(Arrays.asList(others));
        }

        public WithSourceFiles andSourceFiles(Collection<SourceFile> files) {
            sourceFiles.addAll(files);
            return this;
        }

        public boolean byPerformingOnEachRound(RoundAction action) throws IOException {
            Processor processor = processor()
                                          .supportingAnnotationTypesNamed(supportedAnnotationTypeNames)
                                          .performingOnEachRound(action);
            return project()
                           .withSourceFiles(sourceFiles)
                           .compileWith(Collections.singleton(processor));
        }

        public boolean byPerformingOnEachRound(Consumer<? super RoundEnvironment> consumer) throws IOException {
            return byPerformingOnEachRound(onRoundEnvironment(consumer));
        }
    }
}
