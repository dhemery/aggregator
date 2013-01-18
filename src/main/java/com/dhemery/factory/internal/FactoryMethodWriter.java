package com.dhemery.factory.internal;

import javax.lang.model.element.Element;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * Writes declarations of factory methods.
 */
public class FactoryMethodWriter {
    private static final String INDENT = "    ";
    private static final String END_OF_LINE = "[\r\n]+";
    private FactoryMethod method;
    private PrintWriter out;

    public void write(FactoryMethod method, PrintWriter out) {
        this.method = method;
        this.out = out;
        writeJavadocComment();
        out.append(INDENT);
        declareModifiers();
        out.append(' ');
        declareTypeParameters();
        out.append(' ');
        declareReturnType();
        out.append(' ');
        declareMethodName();
        out.append('(');
        declareParameters();
        out.append(')');
        declareExceptions();
        out.append(" {\n");
        out.append(INDENT).append(INDENT);
        declareBody();
        out.append("\n");
        out.append(INDENT);
        out.append("}\n");
    }

    private void writeJavadocComment() {
        if(method.javadocComment() == null) return;
        out.format("%s/**%n", INDENT);
        for(String line : commentLines()) {
           out.format("%s*%s%n", INDENT, line);
        }
        out.format("%s*/%n", INDENT);
    }

    private String[] commentLines() {
        return method.javadocComment().split(END_OF_LINE);
    }

    private void declareModifiers() {
        writeList(method.modifiers(), "", " ", "");
    }

    private void declareTypeParameters() {
        writeList(method.typeParameters(), "<", ",", ">");
    }

    private void declareReturnType() {
        out.append(method.returnType());
    }

    private void declareMethodName() {
        out.append(method.name());
    }

    private void declareParameters() {
        Iterator<? extends Element> iterator = method.parameters().iterator();
        while(iterator.hasNext()) {
            declareParameter(iterator.next());
            if(iterator.hasNext()) out.append(", ");
        }
    }

    private void declareParameter(Element parameter) {
        out.format("%s %s", parameter.asType(), parameter.getSimpleName());
    }

    private void declareExceptions() {
        writeList(method.thrownTypes(), "throws ", ", ", "");
    }

    private void declareBody() {
        out.format("return %s.%s(", method.declaringClass(), method.name());
        writeList(method.parameters(), "", ", ", "");
        out.format(");");
    }

    private <T> void writeList(Iterable<T> items, String prefix, String separator, String suffix) {
        Iterator<T> iterator = items.iterator();
        if(!iterator.hasNext()) return;
        out.append(prefix);
        while(iterator.hasNext()) {
            out.append(iterator.next().toString());
            if(iterator.hasNext()) out.append(separator);
        }
        out.append(suffix);
    }
}