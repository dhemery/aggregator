package com.dhemery.aggregator.helpers;

import javax.annotation.processing.Processor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class ProjectBuilder {
    private List<SourceFile> sourceFiles = new ArrayList<>();
    private Path sourceDir = Paths.get("sources");
    private Path outputDir = Paths.get("classes");
    private Processor processor;

    public static ProjectBuilder project() {
        return new ProjectBuilder();
    }

    public ProjectBuilder withSourceFile(SourceFile sourceFile) {
        sourceFiles.add(sourceFile);
        return this;
    }

    public ProjectBuilder withProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public boolean compile() throws IOException {
        Path absoluteProjectDir = Dirs.createTemporary();
        Path absoluteSourceDir = sourceDir.isAbsolute() ? sourceDir : absoluteProjectDir.resolve(sourceDir);
        Path absoluteOutputDir = outputDir.isAbsolute() ? outputDir : absoluteProjectDir.resolve(outputDir);
        Dirs.createEmpty(absoluteSourceDir);
        Dirs.createEmpty(absoluteOutputDir);
        Set<File> writtenSourceFiles = sourceFiles.stream()
                                               .map(e -> e.writeTo(absoluteSourceDir))
                                               .map(Path::toFile)
                                               .collect(toSet());
        return new Project(absoluteOutputDir, processor, writtenSourceFiles).compile();
    }
}
