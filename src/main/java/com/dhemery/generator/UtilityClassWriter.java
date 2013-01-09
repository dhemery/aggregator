package com.dhemery.generator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class UtilityClassWriter {
    private final Collection<UtilityMethodWriter> factoryMethods = new HashSet<UtilityMethodWriter>();
    private final String annotationName;
    private final String className;
    private final String description;

    public UtilityClassWriter(TypeElement annotation, Collection<UtilityMethodWriter> utilityMethods) {
        annotationName = annotation.getQualifiedName().toString();
        Generate generatedClass = annotation.getAnnotation(Generate.class);
        className = generatedClass.className();
        description = generatedClass.description();
        this.factoryMethods.addAll(utilityMethods);
    }

    public void write(Filer filer) {
        PrintWriter out = classWriter(filer);

        writeCredits(out);
        declarePackage(out);
        writeJavadocComment(out);
        declareClass(out);
        out.close();
    }

    private void writeCredits(PrintWriter out) {
        out.format("// Source generated %s%n", new Date());
        out.format("// by http://github.com/dhemery/generator%n");
        out.format("// for utility methods annotated with %s%n", annotationName);
    }

    private void declarePackage(PrintWriter out) {
        out.format("package %s;%n%n", packageName());
    }

    private void writeJavadocComment(PrintWriter out) {
        out.append("/**\n");
        out.format("* %s%n", description);
        out.append("*/\n");
    }

    private void declareClass(PrintWriter out) {
        out.format("public class %s {", simpleClassName());
        declareMethods(out);
        out.format("}%n");
    }

    private void declareMethods(PrintWriter out) {
        for(UtilityMethodWriter method : factoryMethods) {
            declareMethod(out, method);
        }
    }

    private void declareMethod(PrintWriter out, UtilityMethodWriter method) {
        out.append('\n');
        method.writeTo(out);
    }

    private PrintWriter classWriter(Filer filer) {
        try {
            JavaFileObject sourceFile = filer.createSourceFile(className);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new RuntimeException("Cannot create source file " + className, cause);
        }
    }

    private String packageName() {
        return className.substring(0, className.lastIndexOf('.'));
    }

    private String simpleClassName() {
        return className.substring(className.lastIndexOf('.') + 1);
    }
}
