package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.FakeDeclaredType;
import com.dhemery.aggregator.helpers.FakeTypeElement;
import org.junit.Ignore;
import org.junit.Test;

import javax.lang.model.type.DeclaredType;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SimplifyingTypeNamerTest {
    @Test
    @Ignore("wip")
    public void simpleNameOccursOnceInTypes() {
        String typeName = "foo.bar.Baz";
        TypeNamer namer = new SimplifyingTypeNamer(setOf(typeName));

        FakeTypeElement typeElement = new FakeTypeElement(typeName);
        DeclaredType type = new FakeDeclaredType(typeElement);

        String simplifiedName = namer.nameOf(type);

        assertThat(simplifiedName, is(type.asElement().getSimpleName()));
    }

    private Set<String> setOf(String... strings) {
        return Stream.of(strings).collect(toSet());
    }
}
