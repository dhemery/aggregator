package com.dhemery.aggregator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

interface TypeNamer {
    String name(TypeMirror type);
    String name(DeclaredType type);

    Set<String> all();
}
