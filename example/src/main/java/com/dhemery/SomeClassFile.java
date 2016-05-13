package com.dhemery;

public class SomeClassFile {
    @UtilityMarker
    public static Object returnsObject() { return null; }

    @UtilityMarker
    public static <T> T returnsVariableType() { return null; }

    @UtilityMarker
    public static <T extends Number> T returnsVariableTypeWithUpperBound() { return null; }
}
