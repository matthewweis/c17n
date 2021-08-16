package io.ignice.c17n.gfx;

import static io.ignice.c17n.gfx.ByteMath.array;

public enum Header implements ByteStreamSource {

    // HEADER
    // (6 bytes, fixed length)
    // HEADER = "GIF87a" or "GIF89a"
    //  "GIF" = 0x47494638
    //   "7a" = 0x3761
    //   "9a" = 0x3961

    GIF87A(array(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)),
    GIF89A(array(0x47, 0x49, 0x46, 0x38, 0x39, 0x61));

    private final byte[] bytes;

    Header(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }
}
