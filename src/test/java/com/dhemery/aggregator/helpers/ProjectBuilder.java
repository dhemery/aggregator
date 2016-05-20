package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import java.nio.file.Path;
import java.util.*;

public class ProjectBuilder {
    private final Path sourceDir;
    private final Path outputDir;
    private final List<SourceFile> sourceFiles = new ArrayList<>();

    private ProjectBuilder(Path sourceDir, Path outputDir) {
        this.sourceDir = sourceDir;
        this.outputDir = outputDir;
    }

    public static ProjectBuilder project() {
        return project(Dirs.createTemporary());
    }

    public static ProjectBuilder project(Path projectDir) {
        return project(projectDir.resolve("sources"), projectDir.resolve("output"));
    }

    private static ProjectBuilder project(Path sourceDir, Path outputDir) {
        return new ProjectBuilder(sourceDir, outputDir);
    }

    public ProjectBuilder with(SourceFile first, SourceFile... others) {
        sourceFiles.add(first);
        sourceFiles.addAll(Arrays.asList(others));
        return this;
    }

    public Project build() {
        return new Project(sourceDir, outputDir, sourceFiles);
    }

    public boolean compile() {
        return build().compile();
    }

    public boolean compileWith(Processor processor) {
        return build().compileWith(processor);
    }
}
