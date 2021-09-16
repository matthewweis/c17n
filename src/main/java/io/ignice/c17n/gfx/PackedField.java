package io.ignice.c17n.gfx;

import io.ignice.c17n.util.ArrayOps;
import io.ignice.c17n.util.SanityOps;
import lombok.NonNull;

public record PackedField(boolean globalColorTableFlag) implements ByteStreamSource {


    // todo xd
//    public PackedField(@NonNull byte[] bytes) {
//        SanityOps.requireNonNull(bytes, "bytes");
//        ByteMath.requireLowerUpperBound(bytes, 2);
//        this.bytes = ArrayOps.lsbPad(bytes, 2);
//    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }

}
