package com.dhemery.aggregator.internal;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class Comment {
    private static final java.lang.String CONTENT_LINE = "%n%1s * ";
    private static final java.lang.String OPEN = "%1$s/**" + CONTENT_LINE;
    private static final java.lang.String CLOSE = "%n%1s */%n";
    private final String source;
    private final String open;
    private final String contentLine;
    private final String close;

    private Comment(String source, String prefix) {
        this.source = source;
        open = format(OPEN, prefix);
        contentLine = format(CONTENT_LINE, prefix);
        close = format(CLOSE, prefix);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(source)
                       .map(toJavadoc())
                       .orElse("");
    }

    private Function<String,String> toJavadoc() {
        return s -> new BufferedReader(new StringReader(s)).lines()
                       .collect(joining(contentLine, open, close));
    }

    static String forClass(String commentText) {
        return withPrefix(commentText, "");
    }

    static String withPrefix(String commentText, String prefix) {
        return new Comment(commentText, prefix).toString();
    }
}
