package com.dhemery.aggregator.helpers;

import java.util.*;

import static java.lang.String.format;

public class SourceFileBuilder {
    private final List<String> bodyLines = new ArrayList<>();

    public static SourceFileBuilder sourceFile() {
        return new SourceFileBuilder();
    }

    public SourceFileBuilder withLine(String line) {
        bodyLines.add(line);
        return this;
    }

    public SourceFileBuilder withLines(String first, String second, String... others) {
        bodyLines.add(first);
        bodyLines.add(second);
        bodyLines.addAll(Arrays.asList(others));
        return this;
    }

    public SourceFileBuilder withLines(List<String> lines) {
        bodyLines.addAll(lines);
        return this;
    }

    public SourceFile forClass(String className) {
        String sourceFileName = format("%s.java", className);
        List<String> sourceLines = new ArrayList<>();
        sourceLines.add(format("public class %s {", className));
        sourceLines.addAll(bodyLines);
        sourceLines.add("}");
        return new SourceFile(sourceFileName, sourceLines);
    }
}
