package io.ignice.c17n.gfx;

import lombok.NonNull;

public record CanvasWidth(@NonNull byte[] bytes) implements ByteStreamSource {

    public CanvasWidth {
        ByteMath.requireLowerUpperBound(bytes, 2);
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }

}
