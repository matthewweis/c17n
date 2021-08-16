package io.ignice.c17n.gfx;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.*;
import java.util.PrimitiveIterator.OfInt;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.stream.IntStream;

// experimental
public interface ByteVector {

    @NonNull
    static ByteVector wrap(@NonNull byte[] bytes) {
        return new ContiguousByteVector(bytes);
    }

    @NonNull
    static ByteVector copy(@NonNull byte[] bytes) {
        return new ContiguousByteVector(Arrays.copyOf(bytes, bytes.length));
    }

    byte get(int i);

    int length();

    default OfInt unsignedIterator() {
        return IntStream.range(0, length())
                .map(this::get)
                .iterator();
    }

    @ToString
    @EqualsAndHashCode
    final class ContiguousByteVector implements ByteVector {
        private final byte[] bytes;

        private ContiguousByteVector(byte[] bytes) {
            this.bytes = bytes;
        }

        public byte get(int i) {
            return bytes[i];
        }

        public int length() {
            return bytes.length;
        }

        /**
         * Builder for {@link ByteVector} with lazy semantics.
         */
        static final class Builder {

            private final List<byte[]> sparseSlices = Collections.synchronizedList(new LinkedList<>());

            private static byte[] flatten(@NonNull List<byte[]> bytes) {
                final int length = bytes.stream().mapToInt(slice -> slice.length).sum();
                final byte[] result = new byte[length];

                int offset = 0;
                for (byte[] slice : bytes) {
                    System.arraycopy(slice, 0, result, offset, slice.length);
                    offset += slice.length;
                }

                return result;
            }

            @ToString
            @EqualsAndHashCode
            private static final class LinkedSliceNode {

                private static final AtomicReferenceFieldUpdater<LinkedSliceNode, LinkedSliceNode> nextUpdater =
                        AtomicReferenceFieldUpdater.newUpdater(LinkedSliceNode.class, LinkedSliceNode.class, "next");
                private final byte[] slice;
                // DO NOT ACCESS DIRECTLY, use the corresponding AtomicReferenceFieldUpdater instead.
                private volatile LinkedSliceNode next;

                private LinkedSliceNode(byte[] slice) {
                    this.slice = slice;
                }

                public @NonNull byte[] slice() {
                    return slice;
                }

                public void setNext(@NonNull LinkedSliceNode node) {
                    for (; ; ) {
                        if (Objects.isNull(nextUpdater.getAndSet(next, node))) {
                            // Do not yield thread during this time.
                        }
                    }
                }

                public @NonNull Optional<LinkedSliceNode> next() {
                    return Optional.ofNullable(nextUpdater.get(next));
                }
            }
        }
    }
}
