package com.dhemery;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@SupportedAnnotationTypes("unused")
public class Examples {
    @Example
    public static Object returnsObject() {
        return null;
    }

    @Example
    public static <T> T returnsVariableType() {
        return null;
    }

    @Example
    public static <T extends Number> T returnsVariableTypeWithUpperBound() {
        return null;
    }

    @Example
    public static <T, R> R hasParametersWithGenericTypes(Function<? super T, ? extends R> ignored) {
        return null;
    }

    @Example
    public static Object throwsExceptions() throws IOException, NullPointerException {
        return null;
    }

    @Example
    public static void returnsVoid() {
    }

    @Example
    public static int returnsInt() { return 42; }

    @Example
    public static void takesPrimitiveTypes(byte b, char c, int i, long l, float f, double d){}

    @Example
    private static void NOT_INCLUDED_BECAUSE_PRIVATE(){}

    @Example
    public void NOT_INCLUDED_BECAUSE_NOT_STATIC(){}

    @Example
    public static synchronized final strictfp void extraModifiers(){}

    /**
     * Here is a javadoc comment.
     * <p>
     * This entire comment will be copied to the aggregate class.
     */
    @Example
    public static void withComment(){}

    @Example
    public static <T extends List<? super Number>> T ridiculousTypeParameter(T whatever){ return null; }

//    @Example
//    public static void citesOtherListType(java.awt.List someList) {}
}
