package com.dhemery.annotation.testing;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this.path, ((SourceFile) o).path());
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
