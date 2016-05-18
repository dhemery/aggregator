package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.helpers.*;
import org.junit.*;

import javax.annotation.processing.Processor;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimplifyingTypeReferencesTest {
    Project project;

    @Before
    public void setup() throws IOException {
        project = new Project();
    }

    @Test
    public void simpleNameOccursOnceInTypes() throws IOException {
        List<String> code = Arrays.asList(
                "public class Simple extends Object {",
                "    public static java.nio.file.Path foo() {",
                "        return null;",
                "    }",
                "}"
        );

        project.addSourceFile(Paths.get("Simple.java"), code);

        List<String> output = new ArrayList<>();
        Processor processor = new ProcessorForTesting(p -> {
            p.annotations().stream().map(String::valueOf).forEach(output::add);
        });

        System.out.println("Output from processor: " + output);
        project.compile(processor);
    }

    private Set<String> setOf(String... strings) {
        return Stream.of(strings).collect(toSet());
    }


}
