package com.dhemery.factory.internal;

import com.dhemery.factory.Factory;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Writes source files for factory classes.
 */
public class FactoryClassWriter {
    private final Filer filer;
    private final FactoryMethodWriter methodWriter;
    private final Comparator<? super FactoryMethod> methodComparator;
    private FactoryClass c;
    private PrintWriter out;

    public FactoryClassWriter(Filer filer, FactoryMethodWriter methodWriter, Comparator<? super FactoryMethod> methodComparator) {
        this.filer = filer;
        this.methodWriter = methodWriter;
        this.methodComparator = methodComparator;
    }

    /**
     * Write a source file for the given factory class.
     * @param c describes the factory class to write
     */
    public void write(FactoryClass c) {
        open(c);
        declarePackage();
        writeJavadocComment();
        declareClass();
        close();
    }

    private void declarePackage() {
        out.format("package %s;%n%n", Names.packageName(c.name()));
    }

    private void writeJavadocComment() {
        out.append("/**\n");
        out.format("* %s%n", c.javadoc());
        out.append("*/\n");
    }

    private void declareClass() {
        declareGenerated();
        out.format("public class %s {", Names.simpleName(c.name()));
        declareConstructor();
        declareMethods();
        out.format("}%n");
    }

    private void declareGenerated() {
        out.format("@javax.annotation.Generated(");
        declareAnnotationElement("value", Factory.class.getName());
        declareAnnotationElement(", comments", "Factory methods annoted with " + c.annotationName());
        declareAnnotationElement(", date", new Date());
        out.append(")\n");
    }

    private void declareAnnotationElement(String name, Object value) {
        out.format("%s=\"%s\"", name, value);
    }

    private void declareConstructor() {
        out.format("%n    private %s(){}%n", Names.simpleName(c.name()));
    }

    private void declareMethods() {
        for(FactoryMethod method : sorted(c.methods())) {
            declareMethod(method);
        }
    }

    private List<FactoryMethod> sorted(Collection<FactoryMethod> methods) {
        List<FactoryMethod> methodList = new ArrayList<>(methods);
        Collections.sort(methodList, methodComparator);
        return methodList;
    }

    private void declareMethod(FactoryMethod method) {
        out.append('\n');
        methodWriter.write(method, out);
    }

    private void open(FactoryClass c) {
        this.c = c;
        try {
            JavaFileObject sourceFile = filer.createSourceFile(c.name());
            out = new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            String message = "Cannot create source file " + c.name()
                            + " for factory methods annotated with " + c.annotationName();
            throw new RuntimeException(message, cause);
        }
    }

    private void close() {
        out.close();
    }
}
