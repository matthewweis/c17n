package io.ignice.c17n.gfx;

import discord4j.core.util.ArrayUtil;
import io.ignice.c17n.util.ArrayOps;
import io.ignice.c17n.util.SanityOps;

import java.nio.ByteBuffer;
import java.util.stream.Stream;

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
            throw new IllegalArgumentException("pixelAspectRatio is often ignored by programs and should not be set");
        }
    }

    @Override
    public byte[] bytes() {
        // todo single buffer
        final byte[] w = canvasWidth.bytes();
        final byte[] h = canvasHeight.bytes();
        final byte[] p = packedField.bytes();
        final byte[] bci = new byte[] { backgroundColorIndex };
        final byte[] par = new byte[] { pixelAspectRatio };
        return Stream.of(w, h, p, bci, par).reduce(ByteMath::concat).orElseThrow();
                //.orElseGet(() -> new byte[0]);
    }
}
