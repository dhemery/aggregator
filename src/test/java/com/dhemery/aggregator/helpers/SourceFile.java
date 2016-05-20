package com.dhemery.aggregator.helpers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class SourceFile {
    private final Path path;
    private final List<String> content;

    public SourceFile(Path relativePath, List<String> content) {
        path = relativePath;
        this.content = Collections.unmodifiableList(content);
    }

    public SourceFile(String relativePath, List<String> content) {
        this(Paths.get(relativePath), content);
    }

    public Path path() {
        return path;
    }

    public List<String> content() {
        return content;
    }
}
