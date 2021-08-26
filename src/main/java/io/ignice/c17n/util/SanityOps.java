package io.ignice.c17n.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class SanityOps {

    public static void requireNonNegative(long n, String arg) {
        if (n < 0) throw new IllegalArgumentException(errorMessage(n, arg, "non-negative"));
    }

    public static void requireNonNullOrNegative(Long n, String arg) {
        requireNonNull(n, arg);
        requireNonNegative(n, arg);
    }

    public static void requirePositive(long n, String arg) {
        if (n <= 0) throw new IllegalArgumentException(errorMessage(n, arg, "positive"));
    }

    public static void requireNegative(long n, String arg) {
        if (n >= 0) throw new IllegalArgumentException(errorMessage(n, arg, "negative"));
    }

    public static <T> T requireNonNull(T obj, String arg) {
        if (obj == null) Thread.dumpStack(); // TODO REMOVE DEBUG
        if (obj == null) throw new NullPointerException(errorMessage(null, arg, "non-null"));
        return obj;
    }

    @NonNull
    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        return Objects.requireNonNullElse(obj, defaultObj);
    }

    private static <T> String errorMessage(T actual, String arg, String expectation) {
        return String.format("%s (= %s) must be a %s value", arg, Objects.toString(actual, "null"), expectation);
    }

//    private static String errorMessage(long actual, String arg, String expectedProperty) {
//        return String.format("%s (= %d) must be a %s value", arg, actual, expectedProperty);
//    }

}
