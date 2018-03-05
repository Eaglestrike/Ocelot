package org.team114.ocelot.util;

import org.team114.lib.geometry.Point;

/**
 * Represents the position and direction of the robot.
 */
public class Pose {
    private final double x;
    private final double y;
    private final double velocity;
    /**
     * The direction the robot is facing, in radians.
     */
    private final double heading;

    /**
     * Constructs a pose from required data.
     * @param x the x location of the robot
     * @param y the y location of the robot
     * @param heading the direction the robot is facing, in radians
     * @param velocity the speed the robot is moving in feet per second
     */
    public Pose(double x, double y, double heading, double velocity) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.velocity = velocity;
    }

    /**
     * Returns the location of the robot.
     * @return the robot location represented by this pose, translated into a point
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * Returns the direction the robot is facing.
     * @return the direction the robot is facing according to this pose in radians
     */
    public double getHeading() {
        return this.heading;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    /**
     * Returns the current speed of the robot.
     * @return speed the robot is moving according to this pose in feet per second
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Transform a point in the same coordinate frame as the Pose is to one in a robot-centric frame.
     *
     * @param p point to be transformed
     * @return The point's equivalent in a right-handed robot-centric frame, the positive X-axis at 0 radians
     * and the positive Y-axis 90 degrees clockwise from that.
     */
    public Point asRobotCoordinates(Point p) {
        //intermediate translation
        double x = p.x() - getX();
        double y = p.y() - getY();

        //rotate by robot-radians
        double rad = -getHeading() + (Math.PI/2); // the delta for the x-axis
        return new Point(x * Math.cos(rad) - y * Math.sin(rad),
                x * Math.sin(rad) + y * Math.cos(rad));

    }

    @Override
    public String toString() {
        return "Pose(X: " + getX() + ", Y: " + getY() + ", H: " + getHeading() + ", V: " + getVelocity() + ")";
    }
}
