package com.dhemery.utility.aggregator.helpers;

import org.junit.Assert;

import static java.lang.String.format;

public interface Consumers {
    static void doNotCall(String s) {
        Assert.fail(format("Unexpectedly called consumer (argument: %s)", s));
    }
}
