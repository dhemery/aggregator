package com.dhemery.aggregator;

import static java.lang.String.format;

@SuppressWarnings("WeakerAccess")
public class AggregatorException extends RuntimeException {
    public AggregatorException(String aggregateName, String aggregateAnnotationName, Throwable cause) {
        super(message(aggregateName, aggregateAnnotationName), cause);
    }

    private static String message(String aggregateName, String aggregateAnnotationName) {
        return format(
                "Cannot create source file %s for methods annotated with %s",
                aggregateName,
                aggregateAnnotationName);
    }
}
