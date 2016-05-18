package com.dhemery.aggregator.helpers;

import java.util.*;

import static java.lang.String.format;

public class SourceFileBuilder {
    private final String className;
    private final List<String> bodyLines = new ArrayList<>();

    public SourceFileBuilder(String className) {
        this.className = className;
    }

    public static SourceFileBuilder sourceFileForClass(String className) {
        return new SourceFileBuilder(className);
    }

    public SourceFileBuilder withLine(String line) {
        return withLines(line);
    }

    public SourceFileBuilder withLines(String... lines) {
        this.bodyLines.addAll(Arrays.asList(lines));
        return this;
    }

    public SourceFile build() {
        String sourceFileName = format("%s.java", className);
        List<String> sourceLines = new ArrayList<>();
        sourceLines.add(format("public class %s {", className));
        sourceLines.addAll(bodyLines);
        sourceLines.add("}");
        return new SourceFile(sourceFileName, sourceLines);
    }
}
