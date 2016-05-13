/**
 * Aggregates specially annotated utility methods
 * into utility classes during compilation.
 * <p>
 * <strong>The Need.</strong>
 * Suppose your code declares numerous utility methods,
 * but the utility methods are scattered across many classes,
 * making them difficult for users to find.
 * To make these scattered utility methods easier to use,
 * you create utility classes to aggregate them.
 * Each method in the utility class forwards invocations
 * to one of your original, scattered utility methods.
 * <p>
 * Over time, you find that aggregating utility methods into utility classes
 * becomes a maintenance nuisance.
 * Sometimes you forget to include an important utility method
 * in your utility classes.
 * Even more often, the documentation of a method in the utility class
 * diverges from the documentation of the original method.
 * <p>
 * To ease this effort, the Utility Aggregator
 * automates the process of aggregating utility methods into utility classes.
 * It finds specially annotated
 * utility methods in your code and automatically generates utility classes
 * to aggregate them.
 * <p>
 * To qualify for automatic aggregation, a utility method must be:
 * <ul>
 * <li>declared {@code public static}.</li>
 * <li>annotated with a "utility aggregator" annotation (see the next section).</li>
 * </ul>
 * <p>
 * <strong>Utility Annotation.</strong>
 * A <em>utility annotation</em> is any annotation
 * that is itself annotated with
 * the {@link com.dhemery.utility.aggregator.SpecifiesAggregatedUtilityClass} meta-annotation.
 * All of the methods annotated by a single utility annotation
 * will be aggregated into a single utility class.
 * The {@code SpecifiesAggregatedUtilityClass} specifies
 * the name of the utility class to generate.
 * <p>
 * <strong>Using the Utility Aggregator</strong>
 * <ul>
 * <li>
 * Declare one or more <em>utility annotations</em>,
 * identifying the utility class to generate for each.
 * </li>
 * <li>
 * Annotate one or more static public methods with your utility annotations.
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
 * Each time the compiler encounters a utility annotation
 * (an annotation that you have annotated with {@code SpecifiesAggregatedUtilityClass}),
 * it notifies the Utility Aggregator.
 * </li>
 * <li>
 * The utility aggregator generates a separate utility class for each utility annotation.
 * </li>
 * <li>
 * For each {@code public static} method marked by a given utility annotation,
 * the generated source file includes a method that:
 * <ul>
 * <li>Has the same signature as the annotated method.
 * <li>Has the same Javadoc comment as the annotated method.
 * <li>Calls the annotated method.
 * <li>Returns the annotated method's result.
 * </ul>
 * </li>
 * <li>
 * After the Utility Aggregator generates a utility class,
 * the java compiler compiles it.
 * </li>
 * </ul>
 * <p><strong>Generating multiple utility classes.</strong>
 * You may define any number of utility annotations.
 * For each utility annotation,
 * this processor creates a utility class
 * that aggregates all of the methods
 * annotated by that utility annotation.
 */
package com.dhemery.utility.aggregator;
