package com.dhemery.utility.aggregator;

import com.dhemery.utility.aggregator.visitors.TypeVisitorMixin;

import javax.lang.model.type.*;
import java.util.Optional;
import java.util.function.Consumer;

class TypeSpy implements TypeVisitorMixin<Void, Consumer<String>> {
    @Override
    public Void defaultAction(TypeMirror e, Consumer<String> action) {
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Consumer<String> action) {
        action.accept(String.valueOf(t.asElement()));
        t.getTypeArguments().forEach(a -> a.accept(this, action));
        t.getEnclosingType().accept(this, action);
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Consumer<String> action) {
        t.getTypeVariables().forEach(v -> v.accept(this, action));
        t.getReturnType().accept(this, action);
        t.getParameterTypes().forEach(p -> p.accept(this, action));
        t.getThrownTypes().forEach(p -> p.accept(this, action));
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Consumer<String> action) {
        t.getLowerBound().accept(this, action);
        t.getUpperBound().accept(this, action);
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Consumer<String> action) {
        Optional.ofNullable(t.getExtendsBound()).ifPresent(s -> s.accept(this, action));
        Optional.ofNullable(t.getSuperBound()).ifPresent(s -> s.accept(this, action));
        return null;
    }
}
