package org.team114.lib.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EpsilonTest {
    @Test
    public void testEpsilonEqualsNearZero() {
        generalEpsilonTest(0);
        generalEpsilonTest(1e-5);
        generalEpsilonTest(-1e-3);
    }

    @Test
    public void testEpsilonNegativeSmall() {
        generalEpsilonTest(-5);
    }

    @Test
    public void testEpsilonPositiveSmall() {
        generalEpsilonTest(7);
    }

    @Test
    public void testEpsilonNegativeLarge() {
        generalEpsilonTest(-1e10 + 31278);
    }

    @Test
    public void testEpsilonPositiveLarge() {
        generalEpsilonTest(1e10 + 3123);
    }


    public void generalEpsilonTest(double start) {
        assertTrue(Epsilon.epsilonEquals(start, start)); //reflexive

        assertFalse(Epsilon.epsilonEquals(start, start + 1)); //converse reflexive
        assertFalse(Epsilon.epsilonEquals(start + 1, start));
        assertFalse(Epsilon.epsilonEquals(start, start - 1));
        assertFalse(Epsilon.epsilonEquals(start - 1, start));

        assertTrue(Epsilon.epsilonEquals(start + 1e-20, start)); //reflexive
        assertTrue(Epsilon.epsilonEquals(start - 1e-20, start));
        assertTrue(Epsilon.epsilonEquals(start, start + 1e-20));
        assertTrue(Epsilon.epsilonEquals(start, start - 1e-20));

        assertTrue(Epsilon.epsilonEquals(start, start + 1, 1.1));
        assertTrue(Epsilon.epsilonEquals(start + 1, start, 1.1)); //select epsilon
        assertTrue(Epsilon.epsilonEquals(start, start - 1, 1.1));
        assertTrue(Epsilon.epsilonEquals(start - 1, start, 1.1));

        assertFalse(Epsilon.epsilonEquals(start, start + 1, 0.99));
        assertFalse(Epsilon.epsilonEquals(start + 1, start, 0.99)); //select epsilon
        assertFalse(Epsilon.epsilonEquals(start, start - 1, 0.99));
        assertFalse(Epsilon.epsilonEquals(start - 1, start, 0.99));
    }
}
