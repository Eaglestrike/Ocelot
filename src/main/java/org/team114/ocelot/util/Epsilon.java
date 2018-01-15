package org.team114.ocelot.util;

public class Epsilon {
    public static double EPSILON = 1e-6;

    /**
     * Compare if two doubles are close enough to equal.
     * @param a the first double
     * @param b the second double
     * @param epsilon the maximum distance apart at which they are considered equal; must be positive
     * @return true if they are "equal" false if not
     */
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return Math.abs(a-b) < epsilon;
    }

    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, EPSILON);
    }
}
