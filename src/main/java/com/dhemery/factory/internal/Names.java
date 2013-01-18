package com.dhemery.factory.internal;

/**
 * Utility methods for extracting parts of type names.
 */
public class Names {
    public static String packageName(String name) {
        return name.substring(0, name.lastIndexOf('.'));
    }

    public static String simpleName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
