package com.dhemery.aggregator.internal;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

interface TypeNamer {
    String nameOf(TypeMirror type);
    String nameOf(DeclaredType type);

    Set<String> fullNames();
}
