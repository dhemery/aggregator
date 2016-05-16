package com.dhemery.aggregator.internal;


import com.dhemery.aggregator.helpers.FakeDeclaredType;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.*;

import javax.lang.model.type.DeclaredType;
import java.util.function.Consumer;

public class MethodWriterVisitDeclaredType {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private Consumer<String> declaration;
    @Mock
    private TypeNamer namer;

    private MethodWriter subject;

    @Before
    public void setup() {
        subject = new MethodWriter(namer);
    }

    @Test
    public void simpleDeclaredType() {
        final String typeName = "foo.bar.Baz";
        DeclaredType simpleDeclaredType = new FakeDeclaredType(typeName);

        context.checking(new Expectations() {{
            allowing(namer).name(with(simpleDeclaredType));
                will(returnValue(typeName));

            oneOf(declaration).accept(typeName);
        }});

        subject.visit(simpleDeclaredType, declaration);
    }
}
