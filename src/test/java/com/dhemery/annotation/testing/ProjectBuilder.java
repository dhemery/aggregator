package com.dhemery.annotation.testing;

import javax.annotation.processing.Processor;
import java.nio.file.Path;
import java.util.*;

public class ProjectBuilder {
    private final Path sourceDir;
    private final Path outputDir;
    private final Set<SourceFile> sourceFiles = new HashSet<>();

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

    public ProjectBuilder withSourceFile(SourceFile file) {
        sourceFiles.add(file);
        return this;
    }

    public ProjectBuilder withSourceFiles(SourceFile file, SourceFile another, SourceFile... others) {
        return withSourceFile(file).withSourceFile(another).withSourceFiles(Arrays.asList(others));
    }

    public ProjectBuilder withSourceFiles(Collection<SourceFile> sourceFiles) {
        this.sourceFiles.addAll(sourceFiles);
        return this;
    }

    public Project build() {
        return new Project(sourceDir, outputDir, sourceFiles);
    }

    public boolean compileWith(Set<Processor> processors) {
        return build().compileWith(processors);
    }
}
