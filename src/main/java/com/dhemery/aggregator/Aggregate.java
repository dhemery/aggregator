package com.dhemery.aggregator;

import java.lang.annotation.*;

/**
 * Marks an annotation as a aggregate annotation.
 * A aggregate annotation marks public static methods as methods
 * to be aggregated by the {@link Aggregator}
 * into a single aggregate class.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Aggregate {
    /**
     * The fully qualified name for the aggregate class.
     *
     * @return the fully qualified name for the aggregate class
     */
    String className();

    /**
     * The content for the aggregate class's JavaDoc comment.
     *
     * @return the content for the aggregate class's JavaDoc comment
     */
    String classComment();
}
