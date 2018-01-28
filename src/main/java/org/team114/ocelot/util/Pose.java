package org.team114.ocelot.util;

import org.team114.lib.geometry.Point;

public class Pose {
    private final double x;
    private final double y;
    private final double vel;
    /**
     * The direction it is facing, in radians.
     */
    private final double heading;

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
}
