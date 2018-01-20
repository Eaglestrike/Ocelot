package org.team114.lib.pathgenerator;

/**
 * A polynomial that is also usable as a spline.
 */
public class PolynomialSpline extends Polynomial implements ParametricOneVariableSpline {

    public PolynomialSpline(double[] a) {
        super(a);
    }
    
    public PolynomialSpline(double[] a, boolean copy) {
        super(a, copy);
    }

    @Override
    public double at(double t) {
        if (t < 0 || t > 1) {
            throw new IndexOutOfBoundsException("The parameter must be between 0 and 1.");
        }
        return super.eval(t);
    }

    @Override
    public double dfdt(double t) {
        if (t < 0 || t > 1) {
            throw new IndexOutOfBoundsException("The parameter must be between 0 and 1.");
        }
        return super.ddx().eval(t);
    }
}
