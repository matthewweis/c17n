package io.ignice.c17n.gfx;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.Objects;

// todo lots of tests needed to confirm
// experimental
public class ByteMath {

    private static final Logger log = LoggerFactory.getLogger(ByteMath.class);

    private static final int MIN_U8 = Byte.toUnsignedInt(u8(0x00));
    private static final int MAX_U8 = Byte.toUnsignedInt(u8(0xFF));

    // todo make test (and make many more!)
//    public static void main(String[] args) {
//        if (MIN_U8 != 0x00) throw new IllegalStateException("TopType MIN SHOULD BE 0, but got " + MIN_U8);
//        if (MAX_U8 != 0xFF) throw new IllegalStateException("TopType MAX SHOULD BE 255, but got " + MAX_U8);
//        IntStream.rangeClosed(MIN_U8, MAX_U8).forEach(ByteMath::requireByte);
//    }

    private final ByteOrder order;

    public ByteMath(ByteOrder byteOrder) {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static byte[] unpack4u8(int u8x4) {
        return array(msb_0(u8x4), msb_1(u8x4), msb_2(u8x4), msb_3(u8x4));
    }

    public static int pack4u8(byte u8w, byte u8x, byte u8y, byte u8z) {
        return shl_24(u8w) & (shl_16(u8x)) & (shl_8(u8y)) & u8z;
    }

    private static int shl_8(int u32) {
        return shl(u32, 8);
    }

    private static int shl_16(int u32) {
        return shl(u32, 16);
    }

    private static int shl_24(int u32) {
        return shl(u32, 24);
    }

    private static int shr_8(int u32) {
        return shr(u32, 8);
    }

    private static int shr_16(int u32) {
        return shr(u32, 16);
    }

    private static int shr_24(int u32) {
        return shr(u32, 24);
    }

    private static int shr(int u32, int k) {
        return u32 >>> k;
    }

    private static int shl(int u32, int k) {
        return u32 << k;
    }

    public static byte msb_0(int u8x4) {
        return lsb_3(u8x4);
    }

    public static byte msb_1(int u8x4) {
        return lsb_2(u8x4);
    }

    public static byte msb_2(int u8x4) {
        return lsb_1(u8x4);
    }

    public static byte msb_3(int u8x4) {
        return u8cast(u8x4);
    }

    public static byte lsb_0(int u8x4) {
        return u8cast(u8x4);
    }

    public static byte lsb_1(int u8x4) {
        return u8cast(shr_8(u8x4));
    }

    public static byte lsb_2(int u8x4) {
        return u8cast(shr_16(u8x4));
    }

    public static byte lsb_3(int u8x4) {
        return u8cast(shr_24(u8x4));
    }

    // not packed u8x4!
    public static byte[] array(int... values) {
        final byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = u8(values[i]);
        }
        return result;
    }

    public static void requireLowerUpperBound(byte[] bytes, int maxBytes) {
        Objects.requireNonNull(bytes);
        if (bytes.length > maxBytes) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static byte u8(int b) {
        requireByte(b);
        return (byte) b;
    }

    public static int u8(byte b) {
        return b;
    }

    private static void requireByte(int b) {
        if (b != (b & 0xFF)) {
            log.warn("cannot convert int {} to u8", b);
            throw new IllegalArgumentException();
        }
    }

    public static byte u8cast(int b) {
        return (byte) b;
    }

    public static byte[] concat(@NonNull ByteStreamSource head, @NonNull ByteStreamSource tail) {
        return concat(head.bytes(), tail.bytes());
    }

    public static byte[] concat(byte[] head, byte[] tail) {
        // TODO fixme with custom impl for Sparse and Dense formats
        final byte[] result = new byte[head.length + tail.length];
        System.arraycopy(head, 0, result, 0, head.length);
        System.arraycopy(tail, 0, result, head.length, tail.length);
        return result;
    }

}
