package io.ignice.c17n.gfx;

import lombok.NonNull;

public record LogicalScreenDescriptor(@NonNull CanvasWidth width,
                                      @NonNull CanvasHeight height) implements ByteStreamSource {

    @Override
    public byte[] bytes() {
        return ByteMath.concat(width, height);
    }
}
