package org.team114.lib.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpsilonTest {

    @ParameterizedTest
    @ValueSource(doubles = {-0, 1e-5, -1e-3, 5, 7, -1e10 + 31278, 1e10 + 3123})
    void testEpsilon(double start) {
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
