package com.dhemery.utility.aggregator;

import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.dhemery.utility.aggregator.UtilityAggregator.types;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class TypeWriter extends SimpleTypeVisitor8<List<String>,List<String>> {
    @Override
    protected List<String> defaultAction(TypeMirror e, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitNoType(NoType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitPrimitive(PrimitiveType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitNull(NullType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitArray(ArrayType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitDeclared(DeclaredType t, List<String> strings) {
        strings.add(String.valueOf(t));
        return strings;
    }

    @Override
    public List<String> visitError(ErrorType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitTypeVariable(TypeVariable t, List<String> strings) {
        return t.getUpperBound().accept(this, strings);
    }

    @Override
    public List<String> visitWildcard(WildcardType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitExecutable(ExecutableType t, List<String> strings) {
        return strings;
    }

    @Override
    public List<String> visitUnknown(TypeMirror t, List<String> strings) {
        return strings;
    }
}
