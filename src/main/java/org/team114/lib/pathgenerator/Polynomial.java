package org.team114.lib.pathgenerator;

import org.team114.lib.util.Epsilon;

import java.util.Arrays;

/**
 *  A class representing a polynomial of arbitrary degree, allowing for evaluation and
 *  differentiation.
 */
public class Polynomial {
    /**
     * The degree of the polynomial. For instance, if the largest term is 5x^8, this will be
     * 8.
     */
    protected int degree;
    /**
     * The coefficients of the polynomial expression.
     */
    protected double[] coefficients;


    /**
     * Construct a polynomial.
     * @param a The array of coefficients to use; each term in the polynomial is a[i]*x^i.
     * @param copyArray if true, the constructor makes a copy of the array before storing.
     */
    protected Polynomial(double[] a, boolean copyArray) {
        if (copyArray) {
            coefficients = Arrays.copyOf(a, a.length);
        } else {
            coefficients = a;
        }

        degree = coefficients.length - 1;
    }

    /**
     * Construct a polynomial, copying the array.
     * @param a The array of coefficients to use; each term in the polynomial is a[i] * x^i.
     */
    protected Polynomial(double[] a) {
        this(a, true);
    }

    /**
     * Evaluate the polynomial at a given value.
     * @param x The input value.
     * @return The value of P(x), the polynomial evaluated at x.
     */
    public double eval(double x) {
        double val = 0;
        for (int i = 0; i < coefficients.length; i++) {
            val += coefficients[i] * Math.pow(x, i);
        }
        return val;
    }

    /**
     * Get the degree of the polynomial. For instance, if the largest term is 5x^8, returns 8.
     * Warning: does not account for zero coefficients.
     * @return The degree of the polynomial.
     */
    public final int degree() {
        return this.degree;
    }

    /**
     * Like degree() but accounts for zero coefficients.
     * @return The degree of the polynomial.
     */
    public final int smartDegree() {
        int deg = degree;
        while (deg > 0 && Epsilon.epsilonEquals(coefficients[deg], 0)) {
            deg--;
        }
        return deg;
    }

    /**
     * Takes the derivative of the polynomial.
     * @return A new polynomial representing the derivative of this one.
     */
    public Polynomial ddx() {
        double[] g = new double[this.coefficients.length - 1];
        for (int i = 1; i < coefficients.length; i++) {
            g[i - 1] = coefficients[i] * i;
        }
        return new Polynomial(g);
    }

    /**
     * Returns the list of coefficients.
     * @return a list of the coefficients
     */
    public double[] getCoefficients() {
        return coefficients.clone();
    }
}
