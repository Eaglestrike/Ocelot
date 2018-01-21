package org.team114.lib.pathgenerator;

/**
 * A hermite spline that has a degree of 3.
 */
public class CubicHermiteSpline extends PolynomialSpline {
    /**
     * Construct a cubic spline from an array of coefficients.
     * @param a The array of coefficients to use; each term in the polynomial is a[i] * x^i.
     */
    public CubicHermiteSpline(double[] a) {
        super(a);
        if (super.degree() != 3) {
            throw new IllegalArgumentException("The passed array would not create a cubic function.");
        }
    }
}
