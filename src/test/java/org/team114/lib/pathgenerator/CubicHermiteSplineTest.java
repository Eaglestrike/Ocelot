package org.team114.lib.pathgenerator;

import org.junit.Test;

import static org.junit.Assert.fail;

public class CubicHermiteSplineTest {

    @Test
    public void testDegreeChecking() {
        try {
            new CubicHermiteSpline(new double[]{7, 3, 5, 1});
            new CubicHermiteSpline(new double[]{0, 3, 5, 0});
            new CubicHermiteSpline(new double[]{0, 0, 0, 0});
        } catch (Exception e) {
            fail();
        }

        try {
            new CubicHermiteSpline(new double[]{7, 3, 5});
            fail();
        } catch (Exception ignored) {
        }

        try {
            new CubicHermiteSpline(new double[]{7, 3, 5, 19, 21});
            fail();
        } catch (Exception ignored) {
        }
    }
}
