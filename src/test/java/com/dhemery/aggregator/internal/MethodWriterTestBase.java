package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.RoundProcessor;
import com.dhemery.aggregator.helpers.SourceFile;
import org.junit.Before;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

class MethodWriterTestBase extends ProcessorTestBase {
    private MethodWriter visitor;

    @Before
    public void setup() {
        visitor = new MethodWriter(simpleNamer());
    }

    private TypeReferences simpleNamer() {
        return new TypeReferences() {
            @Override
            public Set<String> fullNames() {
                return Collections.emptySet();
            }

            @Override
            public String nameOf(TypeMirror type) {
                return String.valueOf(type);
            }

            @Override
            public String nameOf(DeclaredType type) {
                return String.valueOf(type);
            }
        };
    }

    String visitType(SourceFile sourceFile, Function<ExecutableElement, ? extends TypeMirror> typeMapper) throws IOException {
        StringBuilder declaration = new StringBuilder();
        process(sourceFile, by(visiting(typeMapper, declaration::append)));
        return declaration.toString();
    }

    RoundProcessor visiting(Function<ExecutableElement, ? extends TypeMirror> typeExtractor, Consumer<String> action) {
        return (annotations, roundEnvironment, processingEnvironment) -> {
            targetedMethodElements(roundEnvironment).stream()
                    .map(typeExtractor)
                    .forEach(t -> t.accept(visitor, action));
            return false;
        };
    }
}
