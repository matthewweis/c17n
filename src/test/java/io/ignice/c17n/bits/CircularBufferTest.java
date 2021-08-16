package io.ignice.c17n.bits;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

class CircularBufferTest {

    @Test
    @Disabled
    public void sanityCheck() {
        final int[] ints = new int[5];
        Arrays.fill(ints, 0);
        final CircularBuffer<Integer> buffer = CircularBuffer.fromInts(ints);

        // check initial state before advancing buffer
        Assertions.assertArrayEquals(array(0, 0, 0, 0, 0), buffer.backingArray);
        assertValidIndexLookup(0, buffer);

        // check advancements
        requireValidAdvancement(buffer, 0, 1, 1, 0, 0, 0, 0);
        requireValidAdvancement(buffer, 1, 2, 1, 2, 0, 0, 0);
        requireValidAdvancement(buffer, 2, 3, 1, 2, 0, 0, 0);
        requireValidAdvancement(buffer, 3, 4, 1, 2, 3, 0, 0);
        requireValidAdvancement(buffer, 4, 5, 1, 2, 3, 4, 0);
        requireValidAdvancement(buffer, 0, 6, 1, 2, 3, 4, 5);
        requireValidAdvancement(buffer, 1, 7, 6, 2, 3, 4, 5);
        requireValidAdvancement(buffer, 2, 8, 6, 7, 3, 4, 5);
        requireValidAdvancement(buffer, 3, 9, 6, 8, 3, 4, 5);
        requireValidAdvancement(buffer, 4, 10, 6, 8, 9, 4, 5);
        requireValidAdvancement(buffer, 0, 11, 6, 8, 9, 10, 5);
        requireValidAdvancement(buffer, 1, 12, 6, 8, 9, 10, 11);
        requireValidAdvancement(buffer, 2, 13, 12, 8, 9, 10, 11);
        requireValidAdvancement(buffer, 3, 14, 12, 13, 9, 10, 11);
        requireValidAdvancement(buffer, 4, 15, 12, 13, 14, 10, 11);
        requireValidAdvancement(buffer, 0, 16, 12, 13, 14, 15, 11);
        requireValidAdvancement(buffer, 1, 17, 12, 13, 14, 15, 16);
        // circular replacements start kicking in
        // second cycle
        // third cycle
    }

    private void requireValidAdvancement(CircularBuffer<Integer> buffer, int offset, int v, int n1, int n2, int n3, int n4, int n5) {
        buffer.advance(v);
        requireRotEq(buffer, offset, n1, n2, n3, n4, n5);
//        assertValidIndexLookup(offset, buffer);
    }

    private void requireRotEq(CircularBuffer<Integer> buffer, int offset, int n1, int n2, int n3, int n4, int n5) {
        final Integer[] rotatingArray = array(n1, n2, n3, n4, n5);
        System.out.println("PRE");
        System.out.println(Arrays.toString(buffer.backingArray));
        System.out.println(Arrays.toString(Arrays.stream(buffer.backingArray, 0, 5).toArray()));
        Collections.rotate(Arrays.asList(rotatingArray), offset);
        System.out.println("POST");
        System.out.println(Arrays.toString(buffer.backingArray));
        System.out.println(Arrays.toString(Arrays.stream(buffer.backingArray, 0, 5).toArray()));
        Assertions.assertArrayEquals(rotatingArray, buffer.backingArray);
        assertArrayEq(rotatingArray, buffer.backingArray);
    }

    private static void assertArrayEq(Integer[] expected, Integer[] actual) {
        for (int i=0; i < 5; i++) {
            Assertions.assertEquals((int) expected[i], (int) actual[i]);
        }
    }

    private void sanityCheck1(CircularBuffer<Integer> buffer, int n1, int n2, int n3, int n4, int n5) {
        boolean eq = true;
        for (int i=0; i < 5; i++) {
            if (eq) {
                eq = array(n1, n2, n3, n4, n5)[i] == buffer.backingArray[i];
            }
        }
        Assertions.assertTrue(eq);
    }

    private void sanityCheck2(CircularBuffer<Integer> buffer, int n1, int n2, int n3, int n4, int n5) {
        boolean eq = true;
        for (int i=0; i < 5; i++) {
            if (eq) {
                eq = array(n1, n2, n3, n4, n5)[i] == buffer.backingArray[i];
            }
        }
        Assertions.assertTrue(eq);
    }

    private void assertValidIndexLookup(int offset, CircularBuffer<Integer> buffer) {
        final Integer[] backingArray = buffer.backingArray;
        for (int i=0; i < backingArray.length; i++) {
            Assertions.assertEquals(buffer.get(i), backingArray[Math.floorMod(i + offset, backingArray.length)]);
        }
    }

    private Integer[] array(int ... ints) {
        return IntStream.of(ints).boxed().toArray(Integer[]::new);
    }

}