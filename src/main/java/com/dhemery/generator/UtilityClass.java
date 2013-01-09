package com.dhemery.generator;

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
        Generate generatedClass = annotation.getAnnotation(Generate.class);
        name = generatedClass.className();
        description = generatedClass.description();
        this.methods.addAll(utilityMethods);
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
