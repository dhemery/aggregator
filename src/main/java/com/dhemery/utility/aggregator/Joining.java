package com.dhemery.utility.aggregator;

import java.util.StringJoiner;
import java.util.stream.Collector;

class Joining {
    static Collector<String, ?, String> orEmpty(CharSequence delimiter, CharSequence prefix, String suffix) {
        return Collector.of(
                () -> new StringJoiner(delimiter, prefix, suffix).setEmptyValue(""),
                StringJoiner::add,
                StringJoiner::merge,
                String::valueOf
        );
    }
}
