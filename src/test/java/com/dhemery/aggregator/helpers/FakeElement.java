package com.dhemery.aggregator.helpers;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class FakeElement implements Element {
    private final Name simpleName;
    private TypeMirror type;

    public FakeElement(String simpleName) {
        this.simpleName = name(simpleName);
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TypeMirror asType() {
        return type;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Element getEnclosingElement() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ElementKind getKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Modifier> getModifiers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getSimpleName() {
        return simpleName;
    }

    private Name name(String name) {
        return new Name() {
            @Override
            public int length() {
                return name.length();
            }

            @Override
            public char charAt(int index) {
                return name.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return name.subSequence(start, end);
            }

            @Override
            public boolean contentEquals(CharSequence cs) {
                return name.contains(cs);
            }
        };
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }
}
