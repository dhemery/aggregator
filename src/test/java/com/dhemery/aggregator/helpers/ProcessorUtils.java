package com.dhemery.aggregator.helpers;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

public class ProcessorUtils {
    public static Function<? super Element, TypeMirror> RETURN_TYPE = e -> ExecutableElement.class.cast(e).getReturnType();
}
