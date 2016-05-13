package com.dhemery.utility.aggregator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

import static java.lang.String.format;

/**
 * Writes a utility class for a given utility annotation.
 */
class UtilityClass {
    private final String utilityClassName;
    private final String comment;
    private final Collection<UtilityMethod> methods;
    private final String utilityAnnotationName;

    UtilityClass(TypeElement utilityAnnotation, Collection<UtilityMethod> methods) {
        this.methods = methods;
        utilityAnnotationName = utilityAnnotation.getQualifiedName().toString();
        SpecifiesAggregatedUtilityClass classSpecifier = utilityAnnotation.getAnnotation(SpecifiesAggregatedUtilityClass.class);
        utilityClassName = classSpecifier.utilityClassName();
        comment = Comment.forClass(classSpecifier.utilityClassComment());
    }

    void write(Filer filer) {
        PrintWriter out = printWriter(filer);
        out.format("package %s;", packageName())
                .format("%n%n%s", comment)
                .format("%n%s", generator())
                .format("%npublic class %s {", simpleName())
                .format("%nprivate %s(){}", simpleName());
        methods.stream()
                .sorted()
                .forEach(m -> m.write(out));
        out.format("}")
                .close();
    }

    private String generator() {
        return format(
                "@javax.annotation.Generated(value=\"%s\", comments=\"%s\", date=\"%s\")",
                generatorName(),
                generatorComment(),
                new Date());
    }

    private String generatorComment() {
        return format("Utility methods annotated with %s", utilityAnnotationName);
    }

    private String generatorName() {
        return SpecifiesAggregatedUtilityClass.class.getName();
    }

    private String packageName() {
        return utilityClassName.substring(0, utilityClassName.lastIndexOf('.'));
    }

    private PrintWriter printWriter(Filer filer) {
        try {
            JavaFileObject sourceFile = filer.createSourceFile(utilityClassName);
            return new PrintWriter(sourceFile.openWriter());
        } catch (IOException cause) {
            throw new UtilityAggregatorException(utilityClassName, utilityAnnotationName, cause);
        }
    }

    private String simpleName() {
        return utilityClassName.substring(utilityClassName.lastIndexOf('.') + 1);
    }
}
