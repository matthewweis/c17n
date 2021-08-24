package io.ignice.c17n.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

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

}
