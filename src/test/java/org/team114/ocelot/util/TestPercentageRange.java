package org.team114.ocelot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.team114.lib.util.Epsilon.EPSILON;

class TestPercentageRange {
    @Test
    void testDeadBand() {
        PercentageRange percentageRange = new PercentageRange(0.01);
        assertEquals(0.0, percentageRange.deadband(0.02).unscaled(), EPSILON);
        percentageRange = new PercentageRange(0.021);
        assertEquals(0.021, percentageRange.deadband(0.02).unscaled(), EPSILON);
        percentageRange = new PercentageRange(-0.021);
        assertEquals(-0.021, percentageRange.deadband(0.02).unscaled(), EPSILON);
    }

    /**
     * Ensures that the limit of [-1.0, 1.0] is respected.
     */
    @Test
    void testLimit() {
        PercentageRange percentageRange = new PercentageRange(1.0);
        assertEquals(1.0, percentageRange.unscaled(), EPSILON);
        percentageRange = new PercentageRange(1.01);
        assertEquals(1.00, percentageRange.unscaled(), EPSILON);
        percentageRange = new PercentageRange(0.99);
        assertEquals(0.99, percentageRange.unscaled(), EPSILON);
        percentageRange = new PercentageRange(-1.01);
        assertEquals(-1.00, percentageRange.unscaled(), EPSILON);
        percentageRange = new PercentageRange(-0.99);
        assertEquals(-0.990, percentageRange.unscaled(), EPSILON);
    }

    @Test
    void testScaling() {
        PercentageRange pr = new PercentageRange(0.5);
        assertEquals(pr.scaled(10), 5, EPSILON);
        assertEquals(pr.scaled(-1), -0.5, EPSILON);

        pr = new PercentageRange(1 / 3.0);
        assertEquals(pr.scaled(100.0), 100 / 3.0, EPSILON);
        assertEquals(pr.scaled(-23), -23 / 3.0, EPSILON);
    }
}
