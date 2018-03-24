package org.team114.lib.pathgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CubicHermiteSplineTest {

    @Test
    public void testDegreeChecking() {
        // must not throw
        new CubicHermiteSpline(new double[]{7, 3, 5, 1});
        new CubicHermiteSpline(new double[]{0, 3, 5, 0});
        new CubicHermiteSpline(new double[]{0, 0, 0, 0});

        // must throw
        assertThrows(IllegalArgumentException.class, () -> new CubicHermiteSpline(new double[]{7, 3, 5}),
                "Must throw for degree ≠ 3.");

        assertThrows(IllegalArgumentException.class, () -> new CubicHermiteSpline(new double[]{7, 3, 5, 19, 21}),
                "Must throw for degree ≠ 3.");
    }
}
