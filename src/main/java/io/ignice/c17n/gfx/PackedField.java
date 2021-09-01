package io.ignice.c17n.gfx;

public record PackedField(boolean globalColorTableFlag) implements ByteStreamSource {

    public PackedField {
//        ByteMath.requireLowerUpperBound(bytes, 2);
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }

}
