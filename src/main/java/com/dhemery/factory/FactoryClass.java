package com.dhemery.factory;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;

public class FactoryClass {
    private final Collection<FactoryMethod> factoryMethods = new HashSet<FactoryMethod>();
    private final String annotationName;
    private final String className;

    public FactoryClass(TypeElement annotation, Collection<FactoryMethod> factoryMethods) {
        annotationName = annotation.getQualifiedName().toString();
        className = annotation.getAnnotation(FactoryAnnotation.class).value();
        this.factoryMethods.addAll(factoryMethods);
    }

    public void write(Filer filer) {
        PrintWriter out = classWriter(filer);

        out.format("package %s;%n%n", packageName());
        out.append("/**\n");
        out.format("* Factory methods annotated with %s%n", annotationName);
        out.append("*/\n");
        out.format("public class %s {", simpleClassName());
        for(FactoryMethod method : factoryMethods) {
            out.append('\n');
            method.writeTo(out);
        }
        out.format("}%n");
        out.close();
    }

    private PrintWriter classWriter(Filer filer) {
        try {
            JavaFileObject sourceFile = filer.createSourceFile(className);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new RuntimeException("Cannot create factory class " + className, cause);
        }
    }

    private String packageName() {
        return className.substring(0, className.lastIndexOf('.'));
    }

    private String simpleClassName() {
        return className.substring(className.lastIndexOf('.') + 1);
    }
}
