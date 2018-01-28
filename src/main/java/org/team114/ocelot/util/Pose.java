package org.team114.ocelot.util;

import org.team114.lib.geometry.Point;

public class Pose {
    private double x;
    private double y;
    private double vel;
    /**
     * The direction it is facing, in radians.
     */
    private double heading;

    public Pose(double x, double y, double heading, double vel) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.vel = vel;
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    public double getHeading() {
        return this.heading;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getVel() {return vel;}

    /**
     * Transform a point in the same coordinate frame as the Pose is to one in a robot-centric frame.
     *
     * @param p
     * @return The point's equivalent in a right-handed robot-centric frame, the positive X-axis at 0 radians
     * and the positive Y-axis 90 degrees clockwise from that.
     */
    public Point asRobotCoordinates(Point p) {
        //intermediate translation
        double x = p.x() - getX();
        double y = p.y() - getY();

        //rotate by robot-radians
        double rad = -getHeading();
        return new Point(x * Math.cos(rad) - y * Math.sin(rad),
                x * Math.sin(rad) + y * Math.cos(rad));

    }

    @Override
    public String toString() {
        return "Pose(X: " + getX() + ", Y: " + getY() + ", H: " + getHeading() + ", V: " + getVel() + ")";
    }
}
