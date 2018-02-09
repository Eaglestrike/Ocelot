package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;
import org.team114.ocelot.util.Pose;

public class PurePursuitController {


    public static class DriveArcCommand {
        // negative curvature is left, positive is right in accordance with coordinate standards for the x-axis
        public final double curvature;
        public final double vel;

        public DriveArcCommand(double curvature, double vel) {
            this.curvature = curvature;
            this.vel = vel;
        }
    }
    private final PathPointList path;

    private final double lookAheadDistance;

    private boolean isFinished = false;
    private int lastLookAheadPoint;
    private double finishMargin;

    // not currently used, may be needed for a motion profile
    double finalVelocity;
    double pathLength;

    public PurePursuitController(PathPointList path, double lookAheadDistance, double finishMargin, double finalVelocity) {
        this.lookAheadDistance = lookAheadDistance;
        this.path = path;
        this.finishMargin = finishMargin;

        this.lastLookAheadPoint = 0;

        if (false) { // will be used if a motion profile is implemented
            this.finalVelocity = finalVelocity;
            if (this.path.pathComponentList.size() > 0) {
                pathLength = this.path.get(this.path.pathComponentList.size()-1).getDistance();
            } else {
                pathLength = 0;
            }
        }
    }

    /**
     * Returns the closest point on the path to the pose
     */
    private PathComponent getLookAheadPoint(Pose pose) {
        int search = lastLookAheadPoint;
        while (path.get(search).getLocation().dist(pose.getPoint()) < lookAheadDistance &&
                search < path.pathComponentList.size()-1) {
            search++;
        }
        lastLookAheadPoint = search;
        return path.get(search);
    }

    public DriveArcCommand getCommand(Pose pose, double timestamp) {
        // closest point to pose along path
        PathComponent targetComponent = getLookAheadPoint(pose);

        if (isFinished ||
            path.pathComponentList.isEmpty() ||
            path.pathComponentList.get(path.pathComponentList.size() - 1).getLocation().dist(pose.getPoint()) < finishMargin) {
            isFinished = true;
            return new DriveArcCommand(0,0);
        }

        Point targetPoint = targetComponent.getLocation();
        Point robotCentricTarget = pose.asRobotCoordinates(targetPoint);
        double signedCurvature = (2 * robotCentricTarget.x()) / robotCentricTarget.hyp2(new Point(0,0));

        //TODO: replace with a motion profile?
        //TODO: update constant with something in robot settings
        double targetVelocity = Math.min(5.0, 1.5 * targetPoint.dist(pose.getPoint()));

        return new DriveArcCommand(signedCurvature, targetVelocity);
    }

    public boolean isFinished() {
        return isFinished;
    }
}
