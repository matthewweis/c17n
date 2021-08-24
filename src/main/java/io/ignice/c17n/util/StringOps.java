package io.ignice.c17n.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringOps {

    @NonNull
    public static String[] splitWhitespace(@NonNull String string) {
        return string.strip().split("\\s+");
    }

}
