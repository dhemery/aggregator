package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Project {
    private final Set<File> sourceFiles;
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final Processor processor;
    private List<String> options;

    public Project(Path outputDir, Processor processor, Set<File> sourceFiles) {
        this.processor = processor;
        this.sourceFiles = sourceFiles;
        String classPath = Stream.of(outputDir.toString(), classPathFor(getClass()))
                                   .collect(joining(System.getProperty("path.separator")));
        options = Arrays.asList("-classpath", classPath, "-d", outputDir.toString());
    }

    public boolean compile() throws IOException {
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(sourceFiles.stream().toArray(File[]::new));
            JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, null, null, options, null, javaFileObjects);
            Optional.ofNullable(processor)
                    .map(Arrays::asList)
                    .ifPresent(compilationTask::setProcessors);
            return compilationTask.call();
        }
    }

    private static String classPathFor(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }
}
