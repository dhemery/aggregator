package com.dhemery.aggregator.helpers;

import java.util.stream.Stream;

public class Streams {
    @SafeVarargs
    public static <T> Stream<T> streamOf(T first, T... others) {
        return Stream.concat(Stream.of(first), Stream.of(others));
    }
}
