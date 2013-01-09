package com.dhemery.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an annotation as a factory annotation.
 * The value is the name of the factory class to create
 * for methods marked with the factory annotation.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.ANNOTATION_TYPE)
public @interface FactoryAnnotation {
    String value();
}
