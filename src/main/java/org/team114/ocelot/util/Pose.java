package org.team114.ocelot.util;

import org.team114.lib.geometry.Point;

public class Pose {
    private double x;
    private double y;
    /**
     * The direction it is facing, in radians.
     * When heading is zero, it means the robot is facing directly forwards.
     */
    private double heading;

    public Pose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
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
}
