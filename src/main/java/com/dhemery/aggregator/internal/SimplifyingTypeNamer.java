package com.dhemery.aggregator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

class SimplifyingTypeNamer implements TypeNamer {
    private final Map<String, List<String>> typesBySimpleName;

    SimplifyingTypeNamer(Set<String> allTypes) {
        typesBySimpleName = allTypes.stream()
                                    .collect(groupingBy(SimplifyingTypeNamer::simpleName));
    }

    @Override
    public String name(TypeMirror type) {
        if(type instanceof DeclaredType) return name(DeclaredType.class.cast(type));
        return String.valueOf(type);
    }

    @Override
    public String name(DeclaredType type) {
        String qualifiedName = String.valueOf(type);
        String simpleName = type.asElement().getSimpleName().toString();
        return typesBySimpleName.get(simpleName).size() == 1 ? simpleName : qualifiedName;
    }

    @Override
    public String imports() {
        return typesBySimpleName.values().stream()
                       .flatMap(Collection::stream)
                       .filter(t -> !Objects.equals(t, Object.class.getName()))
                       .sorted()
                       .map(i -> format("import %s;%n", i))
                       .collect(joining());
    }

    static String simpleName(String qualifiedName) {
        return qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
    }
}
