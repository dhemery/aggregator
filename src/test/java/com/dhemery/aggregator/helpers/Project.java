package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Project {
    private final Path sourceDir;
    private final List<File> sourceFiles = new ArrayList<>();
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private List<String> options;

    public Project() throws IOException {
        Path projectDir = createProjectDir();
        sourceDir = projectDir.resolve("sources");
        Path outputDir = projectDir.resolve("output");
        Files.createDirectories(sourceDir);
        Files.createDirectories(outputDir);
        String classPath = Stream.of(outputDir.toString(), classPathFor(getClass()))
                                   .collect(joining(System.getProperty("path.separator")));
        options = Arrays.asList("-classpath", classPath, "-d", outputDir.toString());
    }

    private Path createProjectDir() throws IOException {
        Path projectDir = Paths.get("build/dale/stuffs");
        createEmptyDir(projectDir);
        // Path projectDir = Files.createTempDirectory(null).toAbsolutePath();
        // projectDir.toFile().deleteOnExit();
        return projectDir;
    }

    private static void createEmptyDir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir1, IOException exc) throws IOException {
                Files.delete(dir1);
                return FileVisitResult.CONTINUE;
            }
        });
        Files.createDirectories(dir);
    }

    public File[] sourceFiles() {
        return sourceFiles.stream().toArray(File[]::new);
    }

    public boolean compile(Processor processor) throws IOException {
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(sourceFiles.toArray(new File[0]));
            JavaCompiler.CompilationTask compilationTask = compiler.getTask(null, null, null, options, null, javaFileObjects);
            Optional.ofNullable(processor)
                    .map(Arrays::asList)
                    .ifPresent(compilationTask::setProcessors);
            return compilationTask.call();
        }
    }

    public void addSourceFile(Path path, List<String> content) throws IOException {
        Path absolutePath = sourceDir.resolve(path).toAbsolutePath();
        System.out.println("Writing to path: " + absolutePath);
        Files.write(absolutePath, content, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        sourceFiles.add(absolutePath.toFile());
    }

    private static String classPathFor(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }
}
