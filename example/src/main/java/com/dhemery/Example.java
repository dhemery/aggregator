package com.dhemery;

import com.dhemery.utility.aggregator.Aggregate;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Aggregate(className = "com.dhemery.generated.AggregatedExamples", classComment = "Example utility methods.")
public @interface Example {
}
