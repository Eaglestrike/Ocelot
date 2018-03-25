package org.team114.ocelot.util;

import org.team114.ocelot.logging.Errors;

/**
 * Represents a (-1.0, 1.0) range.
 */
public class PercentageRange {
    private static final PercentageRange ZERO = new PercentageRange(0.0);
    private final double percentageRange;

    /**
     * Produces a percentage range from a double between 0 and 1, wrapping and logging
     * values outside this range.
     * @param percentageRange double between -1 and 1
     */
    public PercentageRange(double percentageRange) {
        double limited = limit(percentageRange, 1.0);
        if (percentageRange != limited) {
            Errors.log("Percentage out of range :" + percentageRange);
            this.percentageRange = limited;
        } else {
            this.percentageRange = percentageRange;
        }
    }

    private double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }

    public PercentageRange deadband(double deadband) {
        if (Math.abs(this.percentageRange) <= deadband) {
            return ZERO;
        } else {
            return this;
        }
    }

    /**
     * Returns this as a percentage of a scalar value.
     * @param scalar value to take a percentage of
     * @return scaled value
     */
    public double scaled(double scalar) {
        return scalar * percentageRange;
    }

    /**
     * Returns unscaled percentage value.
     * @return percentage value
     */
    public double unscaled() {
        return percentageRange;
    }
}
