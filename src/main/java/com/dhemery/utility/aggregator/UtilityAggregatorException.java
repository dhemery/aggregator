package com.dhemery.utility.aggregator;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

import static java.lang.String.format;

@SuppressWarnings("WeakerAccess")
public class UtilityAggregatorException extends RuntimeException {
    public UtilityAggregatorException(String utilityClassName, String utilityAnnotationName, Throwable cause) {
        super(message(utilityClassName, utilityAnnotationName), cause);
    }

    private static String message(String utilityClassName, String utilityAnnotationName) {
        return format(
                "Cannot create source file %s for utility methods annotated with %s",
                utilityClassName,
                utilityAnnotationName);
    }
}
