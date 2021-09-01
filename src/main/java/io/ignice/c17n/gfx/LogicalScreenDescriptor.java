package io.ignice.c17n.gfx;

import io.ignice.c17n.util.SanityOps;

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
public record LogicalScreenDescriptor(CanvasWidth canvasWidth,
                                      CanvasHeight canvasHeight,
                                      PackedField packedField,
                                      byte backgroundColorIndex,
                                      byte pixelAspectRatio) implements ByteStreamSource {

    public LogicalScreenDescriptor {
        SanityOps.requireNonNull(canvasWidth, "canvasWidth");
        SanityOps.requireNonNull(canvasHeight, "canvasHeight");
        SanityOps.requireNonNull(packedField, "packedField");
        if (!packedField.globalColorTableFlag() && backgroundColorIndex != 0) {
            throw new IllegalArgumentException("backgroundColorIndex must be 0 if there is no global color table flag");
        }
        if (pixelAspectRatio != 0) {
            throw new IllegalArgumentException("pixelAspectRatio is often unused and should not be set");
        }
    }

    @Override
    public byte[] bytes() {
        final byte[] bytes = new byte[7];

        final byte[] w = canvasWidth.bytes();
        final byte[] h = canvasHeight.bytes();
        final byte[] p = packedField.bytes();
//        Stream.of()
        return bytes;
    }
}
