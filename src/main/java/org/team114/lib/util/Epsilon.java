package org.team114.lib.util;

/**
 * Checks for approximate equality.
 */
public class Epsilon {
    /**
     * Default "close enough" distance. Currently {@value}.
     * */
    public static final double EPSILON = 1e-6;

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

    /**
     * Compare if two doubles are close enough to equal, using {@link #EPSILON} as the maximum distance they can
     * be apart.
     * @param a the first double
     * @param b the second double
     * @return true if they are "equal" false if not
     */
    public static boolean epsilonEquals(double a, double b) {
        return epsilonEquals(a, b, EPSILON);
    }
}
