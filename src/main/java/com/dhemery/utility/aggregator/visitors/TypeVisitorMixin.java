package com.dhemery.utility.aggregator.visitors;

import javax.lang.model.type.*;

public interface TypeVisitorMixin<R, P> extends TypeVisitor<R, P> {
    default R defaultAction(TypeMirror t, P p) {
        return visitUnknown(t, p);
    }

    @Override
    default R visit(TypeMirror t) {
        return visit(t, null);
    }

    @Override
    default R visit(TypeMirror t, P p) {
        return t.accept(this, p);
    }

    @Override
    default R visitArray(ArrayType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitDeclared(DeclaredType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitError(ErrorType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitExecutable(ExecutableType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitIntersection(IntersectionType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitNoType(NoType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitNull(NullType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitPrimitive(PrimitiveType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitTypeVariable(TypeVariable t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitUnion(UnionType t, P p) {
        return defaultAction(t, p);
    }

    @Override
    default R visitUnknown(TypeMirror t, P p) {
        throw new UnknownTypeException(t, p);
    }

    @Override
    default R visitWildcard(WildcardType t, P p) {
        return defaultAction(t, p);
    }
}
