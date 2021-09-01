package io.ignice.c17n.gfx;

import lombok.NonNull;

/**
 * http://giflib.sourceforge.net/whatsinagif/bits_and_bytes.html
 * <p>
 * (1) Canvas Width
 * (2) Canvas Height
 * (3) Packed Field
 * (4) Background Color Index
 * (5) Pixel Aspect Ratio
 * <p>
 * (1)    (2)  (3) (4) (5)
 * _____  _____  __  __  __
 * 0A 00  0A 00  91  00  00
 */
public record GlobalColorTable(@NonNull CanvasWidth width,
                               @NonNull CanvasHeight height) implements ByteStreamSource {

    @Override
    public byte[] bytes() {
        return ByteMath.concat(width, height);
    }
}
