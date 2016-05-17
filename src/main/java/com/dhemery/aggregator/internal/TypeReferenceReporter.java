package com.dhemery.aggregator.internal;

import com.dhemery.aggregator.visitors.TypeVisitorMixin;

import javax.lang.model.type.*;
import java.util.Optional;
import java.util.function.Consumer;

class TypeReferenceReporter implements TypeVisitorMixin<Void, Consumer<String>> {
    @Override
    public Void defaultAction(TypeMirror e, Consumer<String> ignored) {
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Consumer<String> consumer) {
        consumer.accept(String.valueOf(t.asElement()));
        t.getTypeArguments().forEach(a -> a.accept(this, consumer));
        t.getEnclosingType().accept(this, consumer);
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Consumer<String> consumer) {
        t.getTypeVariables().forEach(v -> v.accept(this, consumer));
        t.getReturnType().accept(this, consumer);
        t.getParameterTypes().forEach(p -> p.accept(this, consumer));
        t.getThrownTypes().forEach(p -> p.accept(this, consumer));
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Consumer<String> consumer) {
        t.getLowerBound().accept(this, consumer);
        t.getUpperBound().accept(this, consumer);
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Consumer<String> consumer) {
        Optional.ofNullable(t.getExtendsBound()).ifPresent(s -> s.accept(this, consumer));
        Optional.ofNullable(t.getSuperBound()).ifPresent(s -> s.accept(this, consumer));
        return null;
    }
}
