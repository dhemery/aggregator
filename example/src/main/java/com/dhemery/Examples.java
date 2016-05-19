package com.dhemery;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("SameReturnValue")
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
        throw new IOException();
    }

    @Example
    public static void returnsVoid() {
        System.out.println("");
    }

    @Example
    public static int returnsInt() {
        return new Random().nextInt();
    }

    @Example
    public static void takesPrimitiveTypes(byte b, char c, int i, long l, float f, double d) {
        System.out.format("%s%s%s%s%s%s", b, c, i, l, f, d);
    }

    @Example
    private static void NOT_INCLUDED_BECAUSE_PRIVATE() {
        returnsVoid();
    }

    @Example
    public void NOT_INCLUDED_BECAUSE_NOT_STATIC() {
        returnsVoid();
    }

    @Example
    public static synchronized strictfp void extraModifiers() {
        returnsVoid();
    }

    /**
     * Here is a javadoc comment.
     * <p>
     * This entire comment will be copied to the aggregate class.
     */
    @Example
    public static void withComment() {
        returnsVoid();
    }

    @Example
    public static <T extends List<? super Number>> T ridiculousTypeParameter(T whatever){ return null; }

//    @Example
//    public static void citesOtherListType(java.awt.List someList) {}
}
