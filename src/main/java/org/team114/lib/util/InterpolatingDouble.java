package org.team114.lib.util;

/*
 * The original version of this code was released by FRC Team 254 under the MIT license.
 */

/**
 * Double that can be interpolated using the InterpolatingTreeMap.
 * 
 * @see InterpolatingTreeMap
 */
public class InterpolatingDouble implements Interpolable<InterpolatingDouble>, InverseInterpolable<InterpolatingDouble>,
        Comparable<InterpolatingDouble> {
    public final double value;

    public InterpolatingDouble(Double val) {
        value = val;
    }

    @Override
    public InterpolatingDouble interpolate(InterpolatingDouble other, double x) {
        Double dydx = other.value - value;
        Double searchY = dydx * x + value;
        return new InterpolatingDouble(searchY);
    }

    @Override
    public double inverseInterpolate(InterpolatingDouble upper, InterpolatingDouble query) {
        double upperToLower = upper.value - value;
        if (upperToLower <= 0) {
            return 0;
        }
        double queryToLower = query.value - value;
        if (queryToLower <= 0) {
            return 0;
        }
        return queryToLower / upperToLower;
    }

    @Override
    public int compareTo(InterpolatingDouble other) {
        return Double.compare(value, other.value);
    }

}