package io.ignice.c17n.gfx;

import io.ignice.c17n.util.ArrayOps;
import io.ignice.c17n.util.SanityOps;
import lombok.NonNull;

public record CanvasWidth(@NonNull byte[] bytes) implements ByteStreamSource {

    public CanvasWidth(@NonNull byte[] bytes) {
        SanityOps.requireNonNull(bytes, "bytes");
        ByteMath.requireLowerUpperBound(bytes, 2);
        this.bytes = ArrayOps.lsbPad(bytes, 2);
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }

}
