package com.dhemery.factory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SupportedOptions("factoryClasses")
public class FactoryGenerator extends AbstractProcessor {
    private static final String ANNOTATION_PROPERTY = "dhemery.factory.annotations";
    private final Map<TypeElement,Set<FactoryMethod>> factoryMethodsByAnnotation = new HashMap<TypeElement, Set<FactoryMethod>>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Map<String,String> options = processingEnv.getOptions();
        if(!options.containsKey(ANNOTATION_PROPERTY)) return Collections.emptySet();
        String annotations = options.get("dhemery.factory.annotations");
        return new HashSet<String>(Arrays.asList(annotations));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        factoryMethodsByAnnotation.clear();
        gatherFactoryMethods(annotations, roundEnvironment);
        for(TypeElement annotationElement : factoryMethodsByAnnotation.keySet()) writeFactoryClass(annotationElement);
        return true;
    }

    private void gatherFactoryMethods(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for(TypeElement annotation : annotations) {
            for(Element methodElement : roundEnvironment.getElementsAnnotatedWith(annotation)) {
                FactoryMethod factoryMethod = new FactoryMethod((ExecutableElement) methodElement, processingEnv);
                if(!factoryMethodsByAnnotation.containsKey(annotation)) factoryMethodsByAnnotation.put(annotation, new HashSet<FactoryMethod>());
                factoryMethodsByAnnotation.get(annotation).add(factoryMethod);
            }
        }
    }

    private void writeFactoryClass(TypeElement annotationElement) {
        String factoryClassName = factoryClassNameFor(annotationElement);
        PrintWriter out = classWriter(factoryClassName);

        out.format("// Generated sources for factory annotation %s%n", annotationElement)
           .format("// All factory annotations: %s%n", factoryMethodsByAnnotation.keySet())
           .format("package %s;%n%n", packageNameFor(factoryClassName))
           .format("/**%n* Factory methods.%n*/%n")
           .format("public class %s {%n", simpleClassNameFor(factoryClassName));
        for (FactoryMethod factoryMethod : factoryMethodsByAnnotation.get(annotationElement)) factoryMethod.appendDeclaration(out);
        out.format("}%n");
        out.close();
    }

    private PrintWriter classWriter(String factoryClassName) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(factoryClassName);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new RuntimeException("Cannot create factory class " + factoryClassName, cause);
        }
    }

    private static String simpleClassNameFor(String className) {
        return className.substring(className.lastIndexOf('.') + 1);
    }

    private String factoryClassNameFor(TypeElement annotationElement) {
        return annotationElement.getQualifiedName() + "Methods";
    }

    private static String packageNameFor(String className) {
        return className.substring(0, className.lastIndexOf('.'));
    }
}
