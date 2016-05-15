/**
 * Aggregates specially annotated methods
 * into aggregate classes during compilation.
 * <p>
 * <strong>The Need.</strong>
 * Suppose your code declares numerous utility methods,
 * but the methods are scattered across many classes,
 * making them difficult for users to find.
 * To make these scattered methods easier to use,
 * you create utility classes to aggregate them.
 * Each method in the utility class forwards invocations
 * to one of your original, scattered methods.
 * <p>
 * Over time, you find that aggregating methods into utility classes
 * becomes a maintenance nuisance.
 * Sometimes you forget to include an important method
 * in your utility classes.
 * Even more often, the documentation of a method in the utility class
 * diverges from the documentation of the original method.
 * <p>
 * To ease this effort, the Aggregator
 * automates the process of aggregating methods into classes.
 * It finds specially annotated
 * methods in your code and automatically generates classes
 * to aggregate them.
 * <p>
 * To qualify for automatic aggregation, a method must be:
 * <ul>
 * <li>declared {@code public static}.</li>
 * <li>annotated with an "aggregate annotation" (see the next section).</li>
 * </ul>
 * <p>
 * <strong>Aggregate Annotation.</strong>
 * An <em>aggregate annotation</em> is any annotation
 * marked with the {@link com.dhemery.aggregator.Aggregate} meta-annotation.
 * All of the methods annotated by a single aggregate annotation
 * will be aggregated into a single utility class.
 * The {@code Aggregate} meta-annotation specifies
 * the name of the utility class to generate.
 * <p>
 * <strong>Using the Aggregator</strong>
 * <ul>
 * <li>
 * Declare one or more <em>aggregate annotations</em>,
 * identifying the utility class to generate for each.
 * </li>
 * <li>
 * Annotate one or more static public methods with your aggregate annotations.
 * </li>
 * <li>
 * Add this library to the classpath when you compile.
 * </li>
 * <li>
 * Instruct the Java compiler to process annotations.
 * </li>
 * <li>
 * Compile your code.
 * </li>
 * </ul>
 * <p>
 * When you compile your code:
 * <ul>
 * <li>
 * Each time the compiler encounters an aggregate annotation
 * (an annotation that you have marked with {@code Aggregate}),
 * it notifies the Aggregator.
 * </li>
 * <li>
 * The aggregator generates a separate utility class for each aggregate annotation.
 * </li>
 * <li>
 * For each {@code public static} method marked by a given aggregate annotation,
 * the generated source file includes a method that:
 * <ul>
 * <li>Has the same signature as the annotated method.
 * <li>Has the same Javadoc comment as the annotated method.
 * <li>Calls the annotated method.
 * <li>Returns the annotated method's result.
 * </ul>
 * </li>
 * <li>
 * After the Aggregator generates a utility class,
 * the java compiler compiles it.
 * </li>
 * </ul>
 * <p><strong>Generating multiple utility classes.</strong>
 * You may define any number of aggregate annotations.
 * For each aggregate annotation,
 * this processor creates a utility class
 * that aggregates all of the methods
 * annotated by that annotation.
 */
package com.dhemery.aggregator;
