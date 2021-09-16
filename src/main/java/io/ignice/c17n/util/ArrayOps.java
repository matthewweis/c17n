package io.ignice.c17n.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;

@UtilityClass
public class ArrayOps {
    public static <T> T head(@NonNull T[] array, T fallback) {
        return array.length > 0 ? array[0] : fallback;
    }

    public static <T> T[] tail(@NonNull T[] array) {
        return array.length > 1 ? copyOfRange(array, 1, array.length) : copyOf(array, 0);
    }

    public static <T> T[] lsbPad(T[] array, int minLength) {
        SanityOps.requireNonNull(array, "array");
        if (array.length >= minLength) {
            return array;
        } else {
            final int delta = minLength - array.length;
            @SuppressWarnings("unchecked")
            final T[] unchecked = (T[]) new Object[delta];
            System.arraycopy(array, 0, unchecked, delta, array.length);
            return unchecked;
        }
    }

    public static byte[] lsbPad(byte[] array, int minLength) {
        SanityOps.requireNonNull(array, "array");
        if (array.length >= minLength) {
            return array;
        } else {
            final int delta = minLength - array.length;
            final byte[] unchecked = new byte[delta];
            System.arraycopy(array, 0, unchecked, delta, array.length);
            return unchecked;
        }
    }

}
