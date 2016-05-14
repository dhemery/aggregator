package com.dhemery.utility.aggregator;

import java.lang.annotation.*;

/**
 * Marks an annotation as a utility annotation.
 * A utility annotation marks public static methods as utility methods
 * to be aggregated by the {@link UtilityAggregator}.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Aggregate {
    /**
     * The fully qualified name for the utility class.
     *
     * @return the fully qualified name for the utility class
     */
    String className();

    /**
     * The content for the utility class's JavaDoc comment.
     *
     * @return the content for the utility class's JavaDoc comment
     */
    String classComment();
}
