package com.dhemery;

import com.dhemery.utility.aggregator.SpecifiesAggregatedUtilityClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@SpecifiesAggregatedUtilityClass(utilityClassName = "com.dhemery.generated.Utilities", utilityClassComment = "Example utility methods.")
public @interface UtilityMarker {
}
