package com.dhemery.annotation.testing;

import javax.annotation.processing.Processor;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Project {
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final Path sourceDir;
    private final Path outputDir;
    private final Set<SourceFile> sourceFiles;

    public Project(Path sourceDir, Path outputDir, Set<SourceFile> sourceFiles) {
        this.sourceDir = sourceDir.toAbsolutePath();
        this.outputDir = outputDir.toAbsolutePath();
        this.sourceFiles = sourceFiles;
    }

    public boolean compileWith(Set<Processor> processors) {
        Dirs.createEmpty(outputDir);
        try (StandardJavaFileManager fileManager = fileManager()) {
            return compilationTask(fileObjects(fileManager), processors)
                           .call();
        } catch (IOException cause) {
            throw new RuntimeException("Could not compile project", cause);
        }
    }

    private String classPath() {
        return Stream.of(outputDir.toString(), classPathFor(getClass()))
                       .collect(joining(System.getProperty("path.separator")));
    }

    private static String classPathFor(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    private CompilationTask compilationTask(Iterable<? extends JavaFileObject> javaFileObjects, Set<Processor> processors) {
        CompilationTask task = compiler.getTask(null, null, null, options(), null, javaFileObjects);
        task.setProcessors(processors);
        return task;
    }

    private StandardJavaFileManager fileManager() {
        return compiler.getStandardFileManager(null, null, null);
    }

    private Iterable<? extends JavaFileObject> fileObjects(StandardJavaFileManager fileManager) {
        return fileManager.getJavaFileObjects(writtenSourceFiles());
    }

    private List<String> options() {
        return Arrays.asList("-classpath", classPath(), "-d", outputDir.toString());
    }

    private void write(SourceFile sourceFile) {
        Path fullPath = sourceDir.resolve(sourceFile.path());
        try {
            Files.write(fullPath, sourceFile.content(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write project source file " + fullPath, e);
        }
    }

    private File[] writtenSourceFiles() {
        Dirs.createEmpty(sourceDir);
        return sourceFiles.stream()
                       .peek(this::write)
                       .map(SourceFile::path)
                       .map(sourceDir::resolve)
                       .map(Path::toFile)
                       .toArray(File[]::new);
    }
}
