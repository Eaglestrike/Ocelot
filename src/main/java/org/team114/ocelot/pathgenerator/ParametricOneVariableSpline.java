package org.team114.ocelot.pathgenerator;

public interface ParametricOneVariableSpline {
    /**
     * Get the value of the internal function.
     * @param t The parameter from 0 to 1 inclusive, representing the percent through the spline.
     * @return The value of the spline.
     */
    double at(double t);

    /**
     * Get the value of the first derivative.
     * @param t The parameter from 0 to 1 inclusive, representing the percent through the spline.
     * @return The value of the derivative at that point.
     */
    double dfdt(double t);
}
