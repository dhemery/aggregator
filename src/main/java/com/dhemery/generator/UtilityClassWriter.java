package com.dhemery.generator;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class UtilityClassWriter {
    private final Filer filer;
    private final UtilityMethodWriter methodWriter;
    private UtilityClass c;
    private PrintWriter out;

    public UtilityClassWriter(Filer filer, UtilityMethodWriter methodWriter) {
        this.filer = filer;
        this.methodWriter = methodWriter;
    }

    public void write(UtilityClass c) {
        open(c);
        writeCredits();
        declarePackage();
        writeJavadocComment();
        declareClass();
        close();
    }

    private void writeCredits() {
        out.format("// Source generated %s%n", new Date());
        out.format("// by http://github.com/dhemery/generator%n");
        out.format("// for utility methods annotated with %s%n", c.annotationName());
    }

    private void declarePackage() {
        out.format("package %s;%n%n", c.packageName());
    }

    private void writeJavadocComment() {
        out.append("/**\n");
        out.format("* %s%n", c.description());
        out.append("*/\n");
    }

    private void declareClass() {
        out.format("public class %s {", c.simpleName());
        declareConstructor();
        declareMethods();
        out.format("}%n");
    }

    private void declareConstructor() {
        out.format("%n    private %s(){}%n", c.simpleName());
    }

    private void declareMethods() {
        for(UtilityMethod method : c.methods()) {
            declareMethod(method);
        }
    }

    private void declareMethod(UtilityMethod method) {
        out.append('\n');
        methodWriter.write(method, out);
    }

    private void open(UtilityClass c) {
        this.c = c;
        try {
            JavaFileObject sourceFile = filer.createSourceFile(c.qualifiedName());
            out = new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            String message = "Cannot create source file " + c.qualifiedName()
                            + " for methods annotated by " + c.annotationName();
            throw new RuntimeException(message, cause);
        }
    }

    private void close() {
        out.close();
    }
}
