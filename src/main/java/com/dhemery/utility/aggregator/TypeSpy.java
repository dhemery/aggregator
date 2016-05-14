package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.Optional;
import java.util.function.Consumer;

import static java.lang.String.format;

class TypeSpy extends SimpleTypeVisitor8<Void, Consumer<String>> {
    @Override
    protected Void defaultAction(TypeMirror e, Consumer<String> action) {
        action.accept(format("!! %s of unhandled TypeMirror class %s", e, e.getClass().getSimpleName()));
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
    public Void visitNoType(NoType t, Consumer<String> action) {
        return null;
    }

    @Override
    public Void visitNull(NullType t, Consumer<String> action) {
        return null;
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, Consumer<String> action) {
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

    @Override
    public Void visitIntersection(IntersectionType t, Consumer<String> action) {
        return super.visitIntersection(t, action);
    }

    @Override
    public Void visitUnion(UnionType t, Consumer<String> action) {
        return super.visitUnion(t, action);
    }

    @Override
    public Void visitArray(ArrayType t, Consumer<String> action) {
        return super.visitArray(t, action);
    }

    @Override
    public Void visitError(ErrorType t, Consumer<String> action) {
        return super.visitError(t, action);
    }

    @Override
    public Void visitUnknown(TypeMirror t, Consumer<String> action) {
        return super.visitUnknown(t, action);
    }
}
