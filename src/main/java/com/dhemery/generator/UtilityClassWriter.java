package com.dhemery.generator;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.dhemery.generator.Names.packageName;
import static com.dhemery.generator.Names.simpleName;

public class UtilityClassWriter {
    private final Filer filer;
    private final UtilityMethodWriter methodWriter;
    private final Comparator<? super UtilityMethod> methodComparator;
    private UtilityClass c;
    private PrintWriter out;

    public UtilityClassWriter(Filer filer, UtilityMethodWriter methodWriter, Comparator<? super UtilityMethod> methodComparator) {
        this.filer = filer;
        this.methodWriter = methodWriter;
        this.methodComparator = methodComparator;
    }

    public void write(UtilityClass c) {
        open(c);
        declarePackage();
        writeJavadocComment();
        declareClass();
        close();
    }

    private void declarePackage() {
        out.format("package %s;%n%n", packageName(c.name()));
    }

    private void writeJavadocComment() {
        out.append("/**\n");
        out.format("* %s%n", c.description());
        out.append("*/\n");
    }

    private void declareClass() {
        declareGenerated();
        out.format("public class %s {", simpleName(c.name()));
        declareConstructor();
        declareMethods();
        out.format("}%n");
    }

    private void declareGenerated() {
        out.format("@javax.annotation.Generated(");
        declareAnnotationElement("value", Generate.class.getName());
        declareAnnotationElement(", comments", "Utility methods annoted with " + c.annotationName());
        declareAnnotationElement(", date", new Date());
        out.append(")\n");
    }

    private void declareAnnotationElement(String name, Object value) {
        out.format("%s=\"%s\"", name, value);
    }

    private void declareConstructor() {
        out.format("%n    private %s(){}%n", simpleName(c.name()));
    }

    private void declareMethods() {
        for(UtilityMethod method : sorted(c.methods())) {
            declareMethod(method);
        }
    }

    private List<UtilityMethod> sorted(Collection<UtilityMethod> methods) {
        List<UtilityMethod> methodList = new ArrayList<>(methods);
        Collections.sort(methodList, methodComparator);
        return methodList;
    }

    private void declareMethod(UtilityMethod method) {
        out.append('\n');
        methodWriter.write(method, out);
    }

    private void open(UtilityClass c) {
        this.c = c;
        try {
            JavaFileObject sourceFile = filer.createSourceFile(c.name());
            out = new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            String message = "Cannot create source file " + c.name()
                            + " for utility methods annotated with " + c.annotationName();
            throw new RuntimeException(message, cause);
        }
    }

    private void close() {
        out.close();
    }
}
