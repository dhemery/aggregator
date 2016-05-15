package com.dhemery.utility.aggregator.visitors;

import javax.lang.model.element.*;

public interface ElementVisitorMixin<R,P> extends ElementVisitor<R,P> {
    default R defaultAction(Element e, P p) {
        return visitUnknown(e, p);
    }

    @Override
    default R visit(Element e, P p) {
        return e.accept(this, p);
    }

    @Override
    default R visit(Element e) {
        return visit(e, null);
    }

    @Override
    default R visitPackage(PackageElement e, P p) {
        return defaultAction(e, p);
    }

    @Override
    default R visitType(TypeElement e, P p) {
        return defaultAction(e, p);
    }

    @Override
    default R visitVariable(VariableElement e, P p) {
        return defaultAction(e, p);
    }

    @Override
    default R visitExecutable(ExecutableElement e, P p) {
        return defaultAction(e, p);
    }

    @Override
    default R visitTypeParameter(TypeParameterElement e, P p) {
        return defaultAction(e, p);
    }

    @Override
    default R visitUnknown(Element e, P p) {
        throw new UnknownElementException(e, p);
    }
}
