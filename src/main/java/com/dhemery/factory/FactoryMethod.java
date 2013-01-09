package com.dhemery.factory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FactoryMethod {
    private final ExecutableElement methodElement;
    private final ProcessingEnvironment environment;

    public FactoryMethod(ExecutableElement methodElement, ProcessingEnvironment environment) {
        this.methodElement = methodElement;
        this.environment = environment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactoryMethod that = (FactoryMethod) o;

        return methodElement.equals(that.methodElement);
    }

    @Override
    public int hashCode() {
        return methodElement.hashCode();
    }

    public void appendDeclaration(PrintWriter out) {
        Element declaringClass = methodElement.getEnclosingElement();
        Set<Modifier> modifiers = methodElement.getModifiers();
        List<? extends TypeParameterElement> typeParameters = methodElement.getTypeParameters();
        TypeMirror returnType = methodElement.getReturnType();
        Name methodName = methodElement.getSimpleName();
        List<? extends VariableElement> parameters = methodElement.getParameters();
        List<? extends TypeMirror> thrownTypes = methodElement.getThrownTypes();

        out.append("   /**\n");
        out.append("   ").append(environment.getElementUtils().getDocComment(methodElement)).append('\n');
        out.append("   */\n");
        out.append("    ");
        writeList(out, modifiers, "", " ", " ");
        writeList(out, typeParameters, "<", ",", "> ");
        out.append(returnType.toString()).append(' ');
        out.append(methodName);
        out.append('(');
        writeParameters(out, parameters);
        out.append(')');
        writeList(out, thrownTypes, " throws ", ", ", "");
        out.append(" {\n");
        writeDelegateCall(out, declaringClass, methodName, parameters);
        out.append("    }\n");
    }

    private void writeDelegateCall(PrintWriter out, Element declaringClass, Name methodName, List<? extends VariableElement> parameters) {
        out.append("        return ");
        out.append(declaringClass.toString()).append('.').append(methodName).append('(');
        writeList(out, parameters, "", ", ", "");
        out.append(");\n");
    }

    private void writeParameters(PrintWriter out, List<? extends VariableElement> parameters) {
        Iterator<? extends VariableElement> iterator = parameters.iterator();
        while(iterator.hasNext()) {
            writeParameter(out, iterator.next());
            if(iterator.hasNext()) out.append(", ");
        }
    }

    private void writeParameter(PrintWriter out, VariableElement parameter) {
        TypeMirror parameterType = parameter.asType();
        out.append(parameterType.toString()).append(' ')
                .append(parameter.getSimpleName());
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