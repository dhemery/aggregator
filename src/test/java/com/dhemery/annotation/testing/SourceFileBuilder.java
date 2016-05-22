package com.dhemery.annotation.testing;

import java.util.*;

import static java.lang.String.format;

public class SourceFileBuilder {
    private final List<String> bodyLines = new ArrayList<>();

    private SourceFileBuilder() {
    }

    public static SourceFileBuilder sourceFile() {
        return new SourceFileBuilder();
    }

    public SourceFileBuilder withLine(String line) {
        bodyLines.add(line);
        return this;
    }

    public SourceFileBuilder withLines(String line, String another, String... others) {
        return withLine(line).withLine(another).withLines(Arrays.asList(others));
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
