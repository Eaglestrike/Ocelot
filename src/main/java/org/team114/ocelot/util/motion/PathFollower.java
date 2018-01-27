package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;
import org.team114.lib.pathgenerator.Path;
import org.team114.lib.pathgenerator.Polynomial;
import org.team114.lib.util.Epsilon;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.util.Pose;

public class PathFollower {
    //Easy way to change accuracy/speed of gradient descent
    private static double speed = 20;

    private double lastCall = -1;
    private double k = 0.15 * Math.sqrt(RobotSettings.WHEELBASE_WIDTH);
    private double velocity = 0; //assume initial velocity is 0 for tests

    private Path path;

    public PathFollower(Path path, double timeStamp) {
        this.path = path;
        lastCall = timeStamp;
    }

    public void setCorrectionConstant(double k) {
        this.k = k;
    }

    /**
     * Returns an array of two elements representing the speed of each track on the robot.
     * @param timeStamp
     * @param pose
     * @return {left tread velocity, right tread velocity}
     */
    public double[] tick(double timeStamp, Pose pose) {
        double t = getClosestPointOnSpline(pose.getPoint(), path);

        double n = t + 0.15;
        if (n > path.length())
            n = path.length();

        Point nextPoint = path.getPointAtT(n);

        double error = pose.getHeading() - Math.atan2(nextPoint.y() - pose.getY(), nextPoint.x() - pose.getX());

        //Determine base ratio
        double right = Math.exp(-k * error);
        double left = Math.exp(k * error);

        double distance = nextPoint.dist(pose.getPoint());

        double timePassed = timeStamp - lastCall;

        //take into account the motion profile
        double targetVelocity = new MotionProfile(timePassed, 0, distance, velocity, 0).getVelocity();

        //Take into account turn speed
        if(!Epsilon.epsilonEquals(right, left)) {
            double r = (left + right) * RobotSettings.WHEELBASE_WIDTH / (left - right) + RobotSettings.WHEELBASE_WIDTH;
            if (targetVelocity * targetVelocity / r > RobotSettings.MAX_CENTRI_ACCEL) {
                targetVelocity = Math.sqrt(RobotSettings.MAX_CENTRI_ACCEL * r);
            }
        }

        double ratio = left / right;

        //adjust left and right velocities in order to have the robot velocity be the target
        right = 2 * targetVelocity / (ratio + 1);
        left = 2 * targetVelocity / (1 / ratio + 1);

        lastCall = timeStamp;
        velocity = targetVelocity;

        //Alternative to setting it here, either way the setting code is either here or at robot.java
        return new double[] {left, right};
    }

    /* Gradient Descent logic
     *
     * Like the logic we wrote previously but this version works by recursively finding the best
     * area first in order to reduce error due to peaks. It then does less iterations of gradient
     * descent in order to find a solution within that area.
     */

    private static double[] scanArea(Point p, Polynomial splineX, Polynomial splineY, double low, double high, double rec) {
        if(rec > 0) {
            double[] a = scanArea(p, splineX, splineY, low, high - (high - low) / 2, rec - 1);
            double[] b = scanArea(p, splineX, splineY, low + (high - low) / 2, high, rec - 1);
            return a[0] < b[0] ? a : b;
        }
        double d = (high + low) / 2;
        double a = (high + d) / 2;
        double b = (low + d) / 2;
        Point higher = new Point(splineX.eval(a), splineY.eval(a));
        Point lower = new Point(splineX.eval(b), splineY.eval(b));
        return new double[] {Math.min(p.dist(higher), p.dist(lower)), low, high};
    }

    private static double gradientDescent(Point p, Polynomial splineX, Polynomial splineY, double low, double high, double rep) {
        double d = (high + low) / 2;
        for(int i = 0; i < rep; i++) {
            double a = (high + d) / 2;
            double b = (low + d) / 2;
            Point higher = new Point(splineX.eval(a), splineY.eval(a));
            Point lower = new Point(splineX.eval(b), splineY.eval(b));
            double dist1 = p.dist(higher);
            double dist2 = p.dist(lower);
            if(dist1 < dist2) {
                low = d;
                d = a;
            } else {
                high = d;
                d = b;
            }
        }
        return d;
    }

    private static double getClosestPointOnSpline(Point point, Path path) {
        double bestDistance = point.dist(path.getPointAtT(0));
        double bestPoint = 0;
        for(int i = 0; i < path.length(); i++) {
            double[] scan = scanArea(point, path.getXList().get(i), path.getYList().get(i), 0, 1, (int) Math.sqrt(speed / 2));
            double result = gradientDescent(point, path.getXList().get(i), path.getYList().get(i), scan[1], scan[2], (int) speed / 2);
            double dist = point.dist(path.getPointAtT(i + result));
            if(dist < bestDistance) {
                bestPoint = result + i;
                bestDistance = dist;
            }
        }
        return bestPoint;
    }
}
