package com.dhemery.aggregator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

class SimplifyingTypeReferences implements TypeReferences {
    private final Map<String, List<String>> fullyQualifiedNamesBySimpleName;

    SimplifyingTypeReferences(Set<String> fullyQualifiedNames) {
        fullyQualifiedNamesBySimpleName = fullyQualifiedNames.stream()
                                    .collect(groupingBy(SimplifyingTypeReferences::simpleName));
    }

    @Override
    public String nameOf(TypeMirror type) {
        if(type instanceof DeclaredType) return nameOf(DeclaredType.class.cast(type));
        return String.valueOf(type);
    }

    @Override
    public String nameOf(DeclaredType type) {
        String qualifiedName = String.valueOf(type);
        String simpleName = type.asElement().getSimpleName().toString();
        return fullyQualifiedNamesBySimpleName.get(simpleName).size() == 1 ? simpleName : qualifiedName;
    }

    @Override
    public Set<String> fullNames() {
        return fullyQualifiedNamesBySimpleName.values().stream()
                       .flatMap(Collection::stream)
                       .collect(toSet());
    }

    static String simpleName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }
}
