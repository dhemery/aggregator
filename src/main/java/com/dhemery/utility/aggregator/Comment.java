package com.dhemery.utility.aggregator;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Comment {
    private static final java.lang.String CONTENT_LINE = "%n%1s * ";
    private static final java.lang.String OPEN = "%1$s/**" + CONTENT_LINE;
    private static final java.lang.String CLOSE = "%n%1s */";
    private final List<String> lines;
    private final String open;
    private final String contentLine;
    private final String close;

    private Comment(String source, String prefix) {
        lines = new BufferedReader(new StringReader(source)).lines().collect(toList());
        open = format(OPEN, prefix);
        contentLine = format(CONTENT_LINE, prefix);
        close = format(CLOSE, prefix);
    }

    @Override
    public String toString() {
        return lines.stream()
                       .collect(joining(contentLine, open, close));
    }

    static String forClass(String source) {
        return asJavadoc(source, "");
    }

    static String forMethod(String source) {
        return asJavadoc(source, "    ");
    }

    private static String asJavadoc(String source, String prefix) {
        return new Comment(source, prefix).toString();
    }
}
