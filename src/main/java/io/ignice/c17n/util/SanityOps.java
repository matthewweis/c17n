package io.ignice.c17n.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import reactor.util.annotation.Nullable;

import java.util.Objects;

@UtilityClass
public class SanityOps {

    public static <T> T requireNonNull(T obj, String arg) {
        if (obj == null) throwNPE(errorMessage(null, arg, "non-null"));
        return obj;
    }

    public static long requireNonNegative(long n, String arg) {
        if (n < 0L) throwIllegalArg(errorMessage(n, arg, "non-negative"));
        return n;
    }

    public static long requirePositive(long n, String arg) {
        if (n <= 0L) throwIllegalArg(errorMessage(n, arg, "positive"));
        return n;
    }

    public static long requireNegative(long n, String arg) {
        if (n >= 0L) throwIllegalArg(errorMessage(n, arg, "negative"));
        return n;
    }

    public static int requireNonNegative(int n, String arg) {
        if (n < 0) throwIllegalArg(errorMessage(n, arg, "non-negative"));
        return n;
    }

    public static int requirePositive(int n, String arg) {
        if (n <= 0) throwIllegalArg(errorMessage(n, arg, "positive"));
        return n;
    }

    public static int requireNegative(int n, String arg) {
        if (n >= 0) throwIllegalArg(errorMessage(n, arg, "negative"));
        return n;
    }

    public static long requireNonNullOrNegative(Long n, String arg) {
        requireNonNull(arg, "arg");
        requireNonNull(n, arg);
        requireNonNegative(n, arg);
        return n;
    }

    @NonNull
    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        requireNonNull(defaultObj, "defaultObj"); // fails eagerly unlike Objects#requireNonNullElse(...)
        return (obj != null) ? obj : defaultObj;
    }

    public static <T> T[] requireNonEmpty(T[] array, String arg) {
        requireNonNull(arg, "arg");
        requireNonNull(array, arg);
        Objects.checkIndex(0, array.length);
        return array;
    }

    public static <T> T[] checkSize(T[] array, String arg, int length) {
        requireNonNull(array, arg); // eager failure
        requireNonNegative(length, String.format("%s.length", arg));
        final String subject = String.format("array length match (= %s)", array.length);
        if (length != array.length) throwIllegalArg(errorMessage(length, "length", "exact", subject));
        return array;
    }

    public static <T> T checkIndex(T[] array, String arg, int index) {
        requireNonNull(array, arg); // eager failure
        requireNonNegative(index, String.format("%s.length", arg));
        if (index >= array.length) throwIllegalArg(errorMessage(index, "index", "exclusive", "upper bound"));
        return array[index];
    }

    private static void throwIllegalArg(String errorMessage) {
        requireNonNull(errorMessage, "errorMessage");
        throw new IllegalArgumentException(errorMessage);
    }

    private static void throwNPE(String errorMessage) {
        requireNonNull(errorMessage, "errorMessage");
        throw new NullPointerException(errorMessage);
    }

    private static <T> String errorMessage(@Nullable T actual, String arg, String expectation) {
        return errorMessage(actual, arg, expectation, "value");
    }

    private static <T> String errorMessage(@Nullable T actual, String arg, String expectation, String subject) {
        final String actualString = Objects.toString(actual, "null");
        final String indefiniteArticle = beginsWithVowel(arg) ? "an" : "a";
        return String.format("%s (= %s) must be %s %s %s", arg, actualString, indefiniteArticle, expectation, subject);
    }

    private static boolean beginsWithVowel(String string) {
        requireNonNull(string, "string");
        if (string.isBlank()) return false;
        return isVowel(string.charAt(0));
    }

    /**
     * Checks if param "c" is a vowel (excludes "y" and ignores case).
     * @param c character to check
     * @return true iff param "c" equals "a", "e", "i", "o", or "u" in lowercase or uppercase form.
     */
    private static boolean isVowel(char c) {
        return switch (c) {
            case 'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U' -> true;
            default -> false;
        };
    }

//    private static String errorMessage(long actual, String arg, String expectedProperty) {
//        return String.format("%s (= %d) must be a %s value", arg, actual, expectedProperty);
//    }

}
