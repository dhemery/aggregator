package com.dhemery.generator.internal;

import com.dhemery.generator.Generate;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.HashSet;

public class UtilityClass {
    private final String annotationName;
    private final String name;
    private final String description;
    private final Collection<UtilityMethod> methods = new HashSet<UtilityMethod>();

    public UtilityClass(TypeElement annotation, Collection<UtilityMethod> utilityMethods) {
        annotationName = annotation.getQualifiedName().toString();
        Generate utilityClass = annotation.getAnnotation(Generate.class);
        name = utilityClass.className();
        description = utilityClass.description();
        methods.addAll(utilityMethods);
    }

    public String annotationName() {
        return annotationName;
    }

    public String description() {
        return description;
    }

    public Collection<UtilityMethod> methods() {
        return methods;
    }

    public String packageName() {
        return name.substring(0, name.lastIndexOf('.'));
    }

    public String qualifiedName() {
        return name;
    }

    public String simpleName() {
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
