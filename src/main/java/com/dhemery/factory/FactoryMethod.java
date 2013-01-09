package com.dhemery.factory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FactoryMethod {
    private static final String INDENT = "    ";
    private final Element declaringClass;
    private final String javadocComment;
    private final Set<Modifier> modifiers;
    private final List<? extends TypeParameterElement> typeParameters;
    private final TypeMirror returnType;
    private final Name methodName;
    private final List<? extends VariableElement> parameters;
    private final List<? extends TypeMirror> thrownTypes;

    public FactoryMethod(ExecutableElement methodElement, ProcessingEnvironment environment) {
        declaringClass = methodElement.getEnclosingElement();
        modifiers = methodElement.getModifiers();
        typeParameters = methodElement.getTypeParameters();
        returnType = methodElement.getReturnType();
        methodName = methodElement.getSimpleName();
        parameters = methodElement.getParameters();
        thrownTypes = methodElement.getThrownTypes();
        javadocComment = environment.getElementUtils().getDocComment(methodElement);
    }

    public void writeTo(PrintWriter out) {
        writeJavadocComment(out);
        out.append(INDENT);
        declareModifiers(out);
        out.append(' ');
        declareTypeParameters(out);
        out.append(' ');
        declareReturnType(out);
        out.append(' ');
        declareMethodName(out);
        out.append('(');
        declareParameters(out);
        out.append(')');
        declareExceptions(out);
        out.append(" {\n");
        out.append(INDENT).append(INDENT);
        declareBody(out);
        out.append("\n");
        out.append(INDENT);
        out.append("}\n");
    }

    private void writeJavadocComment(PrintWriter out) {
        if(javadocComment == null) return;
        out.format("/**%n");
        out.format("%s", javadocComment);
        out.format("*/%n");
    }

    private void declareModifiers(PrintWriter out) {
        writeList(out, modifiers, "", " ", "");
    }

    private void declareTypeParameters(PrintWriter out) {
        writeList(out, typeParameters, "<", ",", ">");
    }

    private void declareReturnType(PrintWriter out) {
        out.append(returnType.toString());
    }

    private void declareMethodName(PrintWriter out) {
        out.append(methodName);
    }

    private void declareParameters(PrintWriter out) {
        Iterator<? extends VariableElement> iterator = parameters.iterator();
        while(iterator.hasNext()) {
            declareParameter(out, iterator.next());
            if(iterator.hasNext()) out.append(", ");
        }
    }

    private void declareParameter(PrintWriter out, VariableElement parameter) {
        out.format("%s %s", parameter.asType(), parameter.getSimpleName());
    }

    private void declareExceptions(PrintWriter out) {
        writeList(out, thrownTypes, "throws ", ", ", "");
    }

    private void declareBody(PrintWriter out) {
        out.format("return %s.%s(", declaringClass, methodName);
        writeList(out, parameters, "", ", ", "");
        out.format(");");
    }

    private static <T> void writeList(PrintWriter out, Iterable<T> items, String prefix, String separator, String suffix) {
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