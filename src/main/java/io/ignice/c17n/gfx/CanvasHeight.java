package io.ignice.c17n.gfx;

import lombok.NonNull;

public record CanvasHeight(@NonNull byte[] bytes) implements ByteStreamSource {

    public CanvasHeight {
        ByteMath.requireLowerUpperBound(bytes, 2);
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }

}
