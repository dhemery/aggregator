package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.RoundProcessor;
import org.junit.Before;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

class MethodWriterTestBase extends ProcessorTestBase {
    private MethodWriter writer;

    @Before
    public void setup() {
        writer = new MethodWriter(simpleNamer());
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

    RoundProcessor withEachTypeIn(Function<ExecutableElement, ? extends TypeMirror> typeExtractor, Consumer<String> action) {
        return (annotations, roundEnvironment, processingEnvironment) -> {
            testTargetElements(roundEnvironment).stream()
                    .map(typeExtractor)
                    .forEach(t -> t.accept(writer, action));
            return false;
        };
    }
}
