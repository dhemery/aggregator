package com.dhemery.utility.aggregator;

import javax.lang.model.type.DeclaredType;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

class TypeMapper implements Function<DeclaredType, String> {
    private final Map<String, List<String>> typesBySimpleName;

    TypeMapper(Set<String> allTypes) {
        typesBySimpleName = allTypes.stream()
                                    .collect(groupingBy(TypeMapper::simpleName));
    }

    @Override
    public String apply(DeclaredType type) {
        return String.valueOf(type);
    }

    static String simpleName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }

    String imports() {
        return "";
    }
}
