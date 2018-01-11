package org.team114.robot2018.pathgenerator;

import java.util.ArrayList;
import java.util.List;

import org.team114.robot2018.geometry.Point;

/**
 * An abstract piecewise path section.
 *
 */
public class Path {

    /**
     * A list of the x of the piecewise spline.
     */
    private List<PolynomialSpline> xComponents;

    /**
     * A list of the y of the piecewise spline.
     */
    private List<PolynomialSpline> yComponents;
    
    /**
     * A list of the main points the path goes through. Not completely needed though, so it is only made when needed.
     */
    private List<Point> pointsContained;


    /**
     * Creates a path based on pre-generated lists of of the x and y spline components. This method
     * is not recommended however and if possible the PathFactory should be used.
     * @param xComponents list of x components to use
     * @param yComponents list of y components to use
     * @see PathFactory
     */
    public Path(List<PolynomialSpline> xComponents, List<PolynomialSpline> yComponents) {
        if (xComponents.size() != yComponents.size()) {
            throw new IllegalArgumentException();
        }
        this.xComponents = xComponents;
        this.yComponents = yComponents;
    }

    /**
     * Creates a path based on a the long list of coefficients given by the spline viewer application.
     * Array is ordered as [piecewise section][x component or y component][coefficient]. It is not
     * recommended to use this method if not copy pasting from the spline viewer application.
     * @param coefficients coefficients from the spline viewer
     */
    public Path(double[][][] coefficients) {
        xComponents = new ArrayList<>();
        yComponents = new ArrayList<>();

        for (double[][] part : coefficients) {
            if (part.length != 2) {
                throw new IllegalArgumentException();
            }
            xComponents.add(new PolynomialSpline(part[0]));
            yComponents.add(new PolynomialSpline(part[1]));
        }
    }

    /**
     * Gets the point on path at t.
     * @param t how far along the path to get the point, measured from n through n+1 for each spline
     *          segment, beginning with the 0th.
     * @return The Point at t.
     */
    public Point getPointAtT(double t) {
        if (t < 0 || t > length()) {
            throw new IndexOutOfBoundsException("The parameter must be between 0 and " + length() + ": " + t);
        }
        int component = (int) t;
        //Prevent out of bounds
        if (t == length()) {
            t = 1;
            component = (int) (length() - 1);
        } else {
            t = t - component;
        }
        return new Point(xComponents.get(component).at(t),
                yComponents.get(component).at(t));
    }

    /**
     * Gets the x and y derivatives of the path at a point and returns them in point form.
     * @param t how far along the path to get the point.
     * @return The Point containing the x and y derivatives.
     */
    public Point dydx(double t) {
        if (t < 0 || t > length()) {
            throw new IndexOutOfBoundsException("The parameter must be between 0 and " +
                    length() + ": " + t);
        }
        int component = (int) t;
        if (t == length()) { //Prevent out of bounds
            t = 1;
            component = (int) (length() - 1);
        } else {
            t = t - ((int) t);
        }
        return new Point(xComponents.get(component).dfdt(t), yComponents.get(component).dfdt(t));
    }

    /**
     * Gets the length of the spline. This is done by returning the size of the component list.
     * @return The spline length.
     */
    public double length() {
        assert xComponents.size() == yComponents.size();
        return xComponents.size();
    }
    
    /**
     * Gets a point along a normal line based at t down the spline. If n is negative the point will be
     * 90 degrees clockwise of the direction of the derivative line. If positive, it will be 90 degrees
     * counterclockwise. This can also be interpreted as right (when facing towards increasing t)
     * if positive and left when negative.
     * @param t the location on the Path to extrapolate from.
     * @param n the distance from the point to the path along the normal line.
     * @return The point along the normal line
     */
    public Point getPointAlongNormal(double t, double n) {
        Point base = getPointAtT(t);
        Point dydx = dydx(t);
        
        // atan2 handles infinite or 0 derivatives
        double angle = Math.atan2(dydx.y(), dydx.x()) - Math.PI/2;
        
        return new Point(base.x() + n * Math.cos(angle), base.y() + n * Math.sin(angle));
    }

    /**
     * Since the key points along the spline are not essentials after generation they are not stored.
     * This method returns a generated list of the points along the spline, but assumes parametric
     * segments. If in the event points are essential, a wrapper may be useful.
     * @return A list of all of the end points of the parametric components.
     */
    public List<Point> generatePointList() {
        if (pointsContained != null)
            return pointsContained;
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i <= length(); i++)
            points.add(getPointAtT(i));
        return points;
    }

    public List<PolynomialSpline> getXList(){
        return xComponents;
    }

    public List<PolynomialSpline> getYList(){
        return yComponents;
    }
    
    private double arcLength = -1;
    
    public double getArcLength() {
        return arcLength;
    }
    
    public double loadArcLength(double detail) {
        arcLength = 0;
        Point last = getPointAtT(0);
        for(double i = 0; i < length(); i += 1 / detail) {
            Point next = getPointAtT(i);
            arcLength += next.dist(last);
            last = next;
        }
        return arcLength;
    }
}
