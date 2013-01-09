package com.dhemery.generator;

import java.lang.annotation.*;

/**
 * Indicates that an annotation is a utility generator.
 * A utility generator annotation marks a public static method
 * as a utility method.
 * <p>The {@link UtilityProcessor} processor
 * generates a utility class with the specified name and description.
 * For each utility method,
 * the generated utility class includes a generated method with the same signature.
 * The generated method delegates to the utility method.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Generate {
    /**
     * The fully qualified name of the utility class to generate.
     */
    String className();

    /**
     * A description of the generated utility class,
     * suitable for use as the body of a JavaDoc comment.
     */
    String description() default "";
}
