package com.dhemery.factory;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.dhemery.factory.Factory")
public class FactoryGenerator extends AbstractProcessor {
    private Filer filer;
    private ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        try {
            JavaFileObject sourceFile = filer.createSourceFile("com.dhemery.expressing.Expressions");
            Writer writer = sourceFile.openWriter();
            writer.append("// Generated source\n");
            writer.append("package com.dhemery.expressing;\n\n");
            writer.append("/**\n");
            writer.append("* Factory methods for operators, functions. matchers, and other expression classes.\n");
            writer.append("*/\n");
            writer.append("public class Expressions {\n");
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Factory.class);
            for(Element element : elements) declareMethod((ExecutableElement)element, writer);
            writer.append("}\n");
            writer.close();
        } catch (IOException e) {
        }
        return true;
    }

    private void declareMethod(ExecutableElement element, Writer writer) throws IOException {
        Element declaringClass = element.getEnclosingElement();
        Set<Modifier> modifiers = element.getModifiers();
        List<? extends TypeParameterElement> typeParameters = element.getTypeParameters();
        TypeMirror returnType = element.getReturnType();
        Name methodName = element.getSimpleName();
        List<? extends VariableElement> parameters = element.getParameters();
        List<? extends TypeMirror> thrownTypes = element.getThrownTypes();

        writer.append("   /**\n");
        writer.append("   ").append(processingEnv.getElementUtils().getDocComment(element)).append('\n');
        writer.append("   */\n");
        writer.append("    ");
        writeList(writer, modifiers, "", " ", " ");
        writeList(writer, typeParameters, "<", ",", "> ");
        writer.append(returnType.toString()).append(' ');
        writer.append(methodName);
        writer.append('(');
        writeParameters(writer, parameters);
        writer.append(')');
        writeList(writer, thrownTypes, " throws ", ", ", "");
        writer.append(" {\n");
        writeDelegateCall(writer, declaringClass, methodName, parameters);
        writer.append("    }\n");
    }

    private void writeDelegateCall(Writer out, Element declaringClass, Name methodName, List<? extends VariableElement> parameters) throws IOException {
        out.append("        return ");
        out.append(declaringClass.toString()).append('.').append(methodName).append('(');
        writeList(out, parameters, "", ", ", "");
        out.append(");\n");
    }

    private void writeParameters(Writer out, List<? extends VariableElement> parameters) throws IOException {
        Iterator<? extends VariableElement> iterator = parameters.iterator();
        while(iterator.hasNext()) {
            writeParameter(out, iterator.next());
            if(iterator.hasNext()) out.append(", ");
        }
    }

    private void writeParameter(Writer out, VariableElement parameter) throws IOException {
        TypeMirror parameterType = parameter.asType();
        out.append(parameterType.toString()).append(' ')
                .append(parameter.getSimpleName());
    }

    private static <T> void writeList(Appendable out, Iterable<T> items, String prefix, String separator, String suffix) throws IOException {
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
