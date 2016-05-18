package com.dhemery.aggregator.helpers;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class SourceFile {
    private final Path path;
    private final List<String> content;

    public SourceFile(Path relativePath, List<String> content) {
        path = relativePath;
        this.content = content;
    }

    public SourceFile(String relativePath, List<String> content) {
        this(Paths.get(relativePath), content);
    }

    public Path writeTo(Path sourceDir) {
        Path fullPath = sourceDir.resolve(path);
        try {
            Files.write(fullPath, content, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write project source file " + fullPath, e);
        }
        return fullPath;
    }
}
