package com.dhemery.factory.internal;

import com.dhemery.factory.Factory;

import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents the factory class to generate for a given factory annotation.
 */
public class FactoryClass {
    private final String annotationName;
    private final String name;
    private final String javadoc;
    private final Collection<FactoryMethod> methods = new HashSet<>();

    public FactoryClass(TypeElement annotation, Collection<FactoryMethod> factoryMethods) {
        annotationName = annotation.getQualifiedName().toString();
        Factory factoryClass = annotation.getAnnotation(Factory.class);
        name = factoryClass.className();
        javadoc = factoryClass.javadoc();
        methods.addAll(factoryMethods);
    }

    public String annotationName() {
        return annotationName;
    }

    public String javadoc() {
        return javadoc;
    }

    public Collection<FactoryMethod> methods() {
        return methods;
    }

    public String name() {
        return name;
    }
}
