package com.dhemery.aggregator.helpers;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

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

    public SourceFileBuilder withLines(String first, String... others) {
        this.bodyLines.addAll(Streams.streamOf(first, others).collect(toList()));
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
