package org.team114.ocelot.util;

import org.team114.ocelot.logging.Errors;

/**
 * Represents a (-1.0,1.0) range
 */
public class PercentageRange {
    private final double percentageRange;

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


    public double scaled(double scalar) {
        return scalar * percentageRange;
    }

    public double unscaled() {
        return percentageRange;
    }
}