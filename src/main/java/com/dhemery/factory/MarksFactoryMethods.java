package com.dhemery.factory;

import java.lang.annotation.*;

/**
 * Indicates that an annotation is a factory annotation.
 * A factory annotation marks a public static method
 * as a factory method.
 * <p>The {@link FactoryProcessor} generates a factory class source file
 * for each factory annotation.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MarksFactoryMethods {
    /**
     * The fully qualified name of the factory class to generate.
     */
    String className();

    /**
     * A description suitable for use as the body of a JavaDoc comment
     * for the generated factory class.
     */
    String javadoc();
}
