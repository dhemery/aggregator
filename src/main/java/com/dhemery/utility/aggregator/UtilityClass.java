package com.dhemery.utility.aggregator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Writes a utility class for a given utility annotation.
 */
class UtilityClass {
    private static final List<Modifier> UTILITY_METHOD_MODIFIERS = Arrays.asList(Modifier.STATIC, Modifier.PUBLIC);
    private final TypeElement utilityAnnotation;
    private final Round round;

    UtilityClass(TypeElement utilityAnnotation, Round round) {
        this.utilityAnnotation = utilityAnnotation;
        this.round = round;
    }

    void write(Filer filer) {
        TypeSpy spy = new TypeSpy();
        Set<String> imports = new HashSet<>();
        utilityMethods().forEach(m -> m.methodElement.asType().accept(spy, imports::add));
        PrintWriter out = printWriter(filer);
        out
                .format("package %s;%n%n", packageName());
        imports.stream()
                .sorted()
                .forEach(i -> out.format("import %s;%n", i));
        out
                .format("%n%s", comment())
                .format("%s%n", generator())
                .format("public class %s {", simpleName());
        utilityMethods().sorted()
                .forEach(m -> m.write(out));
        out
                .format("}")
                .close();
    }

    private String comment() {
        return Comment.forClass(classSpecifier().classComment());
    }

    private Aggregate classSpecifier() {
        return utilityAnnotation.getAnnotation(Aggregate.class);
    }

    private String utilityClassName() {
        return classSpecifier().className();
    }

    private Stream<UtilityMethod> utilityMethods() {
        return round.elementsAnnotatedWith(utilityAnnotation)
                       .filter(annotatedElement -> annotatedElement.getKind() == ElementKind.METHOD)
                       .filter(methodElement -> methodElement.getModifiers().containsAll(UTILITY_METHOD_MODIFIERS))
                       .map(ExecutableElement.class::cast)
                       .map(UtilityMethod::new);
    }

    private String generator() {
        return format(
                "@javax.annotation.Generated(value=\"%s\", comments=\"%s\", date=\"%s\")",
                generatorName(),
                generatorComment(),
                new Date());
    }

    private String generatorComment() {
        return format("Utility methods annotated with %s", utilityAnnotationName());
    }

    private String generatorName() {
        return Aggregate.class.getName();
    }

    private String packageName() {
        return packageName(utilityClassName());
    }

    private String packageName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf('.'));
    }

    private String simpleName() {
        return simpleName(utilityClassName());
    }

    private String simpleName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf('.') + 1);
    }

    private PrintWriter printWriter(Filer filer) {
        String utilityClassName = utilityClassName();
        try {
            JavaFileObject sourceFile = filer.createSourceFile(utilityClassName);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new UtilityAggregatorException(utilityClassName(), utilityAnnotationName(), cause);
        }
    }

    private String utilityAnnotationName() {
        return utilityAnnotation.getQualifiedName().toString();
    }
}
