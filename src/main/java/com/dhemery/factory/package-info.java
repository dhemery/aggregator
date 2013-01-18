/**
 * This library provides an annotation processor
 * that generates factory class source files
 * based on specially annotated factory methods.
 *
 * <p><strong>A typical scenario:</strong>
 * Your code declares numerous factory methods,
 * but the factory methods are scattered across many classes,
 * making them difficult for users to find.
 * If you annotate the factory methods with a factory annotation,
 * this processor generates a factory class
 * that "aggregates" all of the annotated methods.
 * Each generated factory method
 * has the same signature as your annotated method.
 * The generated method merely calls your method
 * and returns your method's result.
 *
 * <p>To use this processor:
 * <ul>
 * <li>Declare one or more <em>factory annotations</em> (see below).
 * <li>Annotate one or more factory methods with your factory annotations.
 * <li>Add this library to the classpath when you compile.
 * <li>Instruct the Java compiler to process annotations.
 * </ul>
 *
 * <p>A <strong>factory annotation</strong>
 * is an annotation that is itself annotated with {@link com.dhemery.factory.Factory}.
 *
 * <p><strong>Generating multiple factory classes.</strong>
 * You may define any number of factory annotations.
 * For each factory annotation,
 * this processor creates a factory class
 * that aggregates all of the factory methods
 * annotated by that factory annotation.
 */
package com.dhemery.factory;
