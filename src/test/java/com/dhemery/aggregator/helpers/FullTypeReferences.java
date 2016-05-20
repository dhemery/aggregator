package com.dhemery.aggregator.helpers;

import com.dhemery.aggregator.internal.TypeReferences;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.Set;

public class FullTypeReferences implements TypeReferences {
    @Override
    public Set<String> fullNames() {
        return Collections.emptySet();
    }

    @Override
    public String nameOf(TypeMirror type) {
        return String.valueOf(type);
    }

    @Override
    public String nameOf(DeclaredType type) {
        return String.valueOf(type);
    }
}
