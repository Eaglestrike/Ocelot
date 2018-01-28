package org.team114.lib.pathgenerator;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.factory.LinearSolverFactory_DDRM;
import org.ejml.interfaces.linsol.LinearSolverDense;
import org.team114.lib.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class for creating smooth curves in 2D space, called Paths, and natural splines.
 * The user-friendliest methods are generateC2Path, and generatePath.
 * @see #generateC2Path(List, double, double, double, double)
 * @see #generatePath(List, List, List, List, List)
 */
public class PathFactory {
    /**
     * Creates a cubic spline path through any number of points.
     * @param points A list of y-values to pass through one unit apart.
     * @param ddxStart The derivative of the curve at the start.
     * @param ddxEnd The derivative of the curve at the end.
     * @return A list of containing splines, meant to be placed end-to-end, even though each is
     * valid for t = [0, 1].
     */
    public static ArrayList<PolynomialSpline> CubicC2Spline(List<Double> points, double ddxStart, double ddxEnd) {
        return SmoothSpline(points, Collections.singletonList(ddxStart), Collections.singletonList(ddxEnd));
    }

    /**
     * Creates a smooth spline curve between any number of points.
     * As the number of endpoint derivatives provided increases, so does the continuity of the
     * overall spline, as does the degree of the internal polynomials. Runs in O(m^3 * n^3) time,
     * where n is the number of points and m the number of endpoint derivatives provided.
     *
     * @param points A list of y-values to pass through, one unit apart.
     * @param ddxAtStart A list of endpoint derivatives, the 0th element is the 1st derivative, the 1st the 2nd, etc.
     * @param ddxAtEnd Same as ddxAtStart, but for the end of the curve.
     * @return A list of containing splines, meant to be placed end-to-end, even though each is valid for t = [0,1].
     */
    public static ArrayList<PolynomialSpline> SmoothSpline(List<Double> points, List<Double>
            ddxAtStart, List<Double> ddxAtEnd) {

        if (ddxAtStart.size() != ddxAtEnd.size()) {
            throw new IllegalArgumentException();
        }

        /*
         * A middle segment has 2 constraints for C0 continuity, and j for up to Cj continuity.
         * The first segment has 2 for C0 and n control derivatives. The end segment has 2 for C0,
         * j for up to Cj continuity, and n control derivatives. This gives 2 + j = order (middle
         * segments) and 4 + 2n + j = 2 * order (first and end segments) solving gives an order of
         * 2 + 2n, so we ascertain polyOrder based on the length of passed control Lists.
         */

        int numControlDerivatives = ddxAtEnd.size();
        int polynomialOrder = 2 + 2 * numControlDerivatives; // order is the number of terms
        int numDerivativeContinuityConstraints = polynomialOrder - 2;

        int numSplines = points.size()-1;
        int matrixDim = polynomialOrder * numSplines;

        DMatrixRMaj coeffMatrix = new DMatrixRMaj(matrixDim, matrixDim);
        DMatrixRMaj answerMatrix = new DMatrixRMaj(matrixDim, 1);

        /*
         * For future coders:
         * The spline interpolation solves a large linear system of constraints:
         * A * x = b, where A is a n * n matrix and x and b are 1*n (vertical) vectors.
         * The format of the "x" matrix is as follows:
         *  | a_0,0 |
         *  | a_0,1 |
         *  | a_0,2 |
         *  | ....  |
         *  | a_0,d |
         *  | a_1,0 |
         *  | a_1,1 |
         *  | ....  |
         *  | a_n,d | <-- this is "a", with subscripts "n" and "d" not the word "and"
         * where d is the degree of the polynomial splines and n is the number of splines.
         * Each term of spline a_n is of the form (a_n,i)*x^i
         */

        int currentSpline = 0;

        int columnOffset = 0; //columnOffset is positioned at an0 for the spline we are constraining
        int rowOffset = 0; //row offset is the index of the next constraint row
        //constraints for the 1st spline are:
        // position constraint 1
        coeffMatrix.set(rowOffset, columnOffset, 1);
        answerMatrix.set(rowOffset, 0, points.get(currentSpline));
        rowOffset++;
        // position constraint 2
        for (int i = columnOffset; i < columnOffset + polynomialOrder; i++) {
            coeffMatrix.set(rowOffset, i, 1);
        }
        answerMatrix.set(rowOffset, 0, points.get(currentSpline+1));
        rowOffset++;

        // the start derivative constraints
        // the f^n(0) = is simply ddxCoeff(n,n) * a_n
        for (int deg = 1; deg <= numControlDerivatives; deg++) {
            coeffMatrix.set(rowOffset, columnOffset+deg, ddxCoeff(deg, deg));
            answerMatrix.set(rowOffset, 0, ddxAtStart.get(deg-1));
            rowOffset++;
        }
        columnOffset += polynomialOrder;
        currentSpline++;



        // do the middle splines and most of the last spline
        for (; currentSpline < numSplines; currentSpline++) {
            // position constraint 1
            coeffMatrix.set(rowOffset, columnOffset, 1);
            answerMatrix.set(rowOffset, 0, points.get(currentSpline));
            rowOffset++;
            // position constraint 2
            for (int i = columnOffset; i < columnOffset + polynomialOrder; i++) {
                coeffMatrix.set(rowOffset, i, 1);
            }
            answerMatrix.set(rowOffset, 0, points.get(currentSpline+1));
            rowOffset++;

            // match the "deg'th" derivative at 0 with the last splines at 1
            for (int deg = 1; deg <= numDerivativeContinuityConstraints; deg++) {
                //add this splines degth derivative
                coeffMatrix.set(rowOffset, columnOffset+deg, ddxCoeff(deg, deg));

                // subtract the last splines degth derivative
                for (int power = 0; power < polynomialOrder; power++) {
                    coeffMatrix.set(rowOffset, columnOffset-polynomialOrder+power,
                            -ddxCoeff(power, deg));
                }

                // set to 0
                answerMatrix.set(rowOffset, 0, 1);
                rowOffset++;
            }
            columnOffset += polynomialOrder;
        }
        // reset to very last spline, so we can add endpoint constraints
        currentSpline--;
        columnOffset -= polynomialOrder;
        for (int deg = 1; deg <= numControlDerivatives; deg++) {
            for (int power = 0; power < polynomialOrder; power++) {
                coeffMatrix.set(rowOffset, columnOffset + power, ddxCoeff(power, deg));
                answerMatrix.set(rowOffset, 0, ddxAtEnd.get(deg-1));
            }
            rowOffset++;
        }

        //now we have the matrix, solve it
        DMatrixRMaj outputMatrix = new DMatrixRMaj(matrixDim, 1);
        LinearSolverDense<DMatrixRMaj> solver = LinearSolverFactory_DDRM.general(matrixDim, matrixDim);
        if (!solver.setA(coeffMatrix)) {
            throw new IllegalArgumentException("Singular matrix");
        }
        // Not sure why this would be a problem. It works fine on its own.
        //        if (solver.quality() <= Epsilon.EPSILON) {
        //            throw new IllegalArgumentException("Nearly singular matrix");
        //        }

        solver.setA(coeffMatrix);
        solver.solve(answerMatrix,outputMatrix);

        //decompose into a list of polynomials
        ArrayList<PolynomialSpline> splines = new ArrayList<>(numSplines);
        double[] a = new double[polynomialOrder];
        for (int i = 0; i < numSplines; i++) {
            for (int j = 0; j < polynomialOrder; j++) {
                a[j] = outputMatrix.get(i*polynomialOrder + j, 0);
            }
            splines.add(new PolynomialSpline(a));
        }

        return splines;
    }

    /**
     * Returns the coefficient in the deg'th derivative of the monomial x^exp.
     * @param exp
     * @param deg
     */
    private static int ddxCoeff(int exp, int deg) {
        int total = 1;
        while (deg > 0) {
            total *= exp;
            exp--;
            deg--;
        }
        return total;
    }

    /**
     * Creates a path based on a list of points using CubicC2Spline().
     * Velocity in this context refers to the velocity of the curve, or the spatial
     * movement caused by increasing the parameter of the curve.
     * @param points list of points to build from
     * @param v0 initial velocity
     * @param vf final velocity
     * @param a0 initial angle in radians
     * @param af final angle in radians
     * @return The new path.
     */
    public static Path generateC2Path(List<Point> points, double v0, double vf, double a0, double af) {
        final double rootOneHalf = Math.sqrt(1/2);

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();

        for (Point p : points) {
            x.add(p.x());
            y.add(p.y());
        }

        return new Path(PathFactory.CubicC2Spline(x, rootOneHalf * v0 * Math.cos(a0), rootOneHalf *
                vf * Math.cos(af)),
                PathFactory.CubicC2Spline(y, rootOneHalf * v0 * Math.sin(a0), rootOneHalf * vf *
                        Math.sin(af)));
    }

    /**
     * Creates a path based on a list of points using SmoothSpline().
     * @see PathFactory#SmoothSpline(List, List, List)
     * @param points A list of points to pass through.
     * @param ddxAtStart A list of endpoint derivatives for start of the x component of the spline.
     * @param ddyAtStart A list of endpoint derivatives for start of the y component of the spline.
     * @param ddxAtEnd A list of endpoint derivatives for end of the x component of the spline.
     * @param ddyAtEnd A list of endpoint derivatives for end of the y component of the spline.
     * @return A Path object generated from those points and derivatives.
     */
    public static Path generatePath(List<Point> points, List<Double> ddxAtStart, List<Double>
            ddyAtStart, List<Double> ddxAtEnd, List<Double> ddyAtEnd) {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();

        for (Point p : points) {
            x.add(p.x());
            y.add(p.y());
        }

        return new Path(PathFactory.SmoothSpline(x, ddxAtStart, ddxAtEnd),
                PathFactory.SmoothSpline(y, ddyAtStart, ddyAtEnd));
    }
}
