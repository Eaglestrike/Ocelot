package org.team114.ocelot.util;

import junit.framework.TestCase;

import static org.team114.lib.util.Epsilon.EPSILON;

public class TestPercentageRange extends TestCase {
    public void testDeadBand() {
        PercentageRange percentageRange = new PercentageRange(0.01);
        assertEquals(0.0, percentageRange.deadband(0.02).unscaled(), EPSILON);
        percentageRange = new PercentageRange(0.021);
        assertEquals(0.021, percentageRange.deadband(0.02).unscaled(), EPSILON);
        percentageRange = new PercentageRange(-0.021);
        assertEquals(-0.021, percentageRange.deadband(0.02).unscaled(), EPSILON);
    }

    /**
     * make sure that the limit of [-1.0, 1.0] is respected
     */
    public void testLimit() {
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
}
