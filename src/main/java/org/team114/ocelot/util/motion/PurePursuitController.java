package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;
import org.team114.ocelot.settings.Settings;
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
    private int lastLookAheadIndex;
    
    private double pathLength;

    public PurePursuitController(PathPointList path, double lookAheadDistance) {
        this.lookAheadDistance = lookAheadDistance;
        this.path = path;

        this.lastLookAheadIndex = 0;
        pathLength = this.path.goalComponent.getDistance();
    }

    /**
     * Returns the closest point on the path to the pose
     */
    private PathComponent getLookAheadPoint(Pose pose) {
        int search = lastLookAheadIndex;
        while (path.get(search).getLocation().dist(pose.getPoint()) < lookAheadDistance &&
                search < path.mainPathPoints.size()-1) {
            search++;
        }
        lastLookAheadIndex = search;
        return path.get(search);
    }

    public DriveArcCommand getCommand(Pose pose) {
        // closest point to pose along path
        PathComponent targetComponent = getLookAheadPoint(pose);

        if (testIsFinished(pose)) {
            return new DriveArcCommand(0, 0);
        }

        Point targetPoint = targetComponent.getLocation();
        Point robotCentricTarget = pose.asRobotCoordinates(targetPoint);
        double signedCurvature = (2 * robotCentricTarget.x()) / robotCentricTarget.hyp2(new Point(0,0));

        //TODO: replace with a motion profile?
        double targetVelocity = Math.min(Settings.PurePursuit.CRUISE_VELOCITY,
                // might get jittery with very few points, can replace lookAheadDistance with actual distance
                Math.max(
                        Settings.PurePursuit.DISTANCE_DECAY_CONSTANT *
                                (lookAheadDistance + pathLength - targetComponent.getDistance()),
                        Settings.PurePursuit.MIN_SPEED
                )
        );

        return new DriveArcCommand(signedCurvature, targetVelocity);
    }


    private boolean testIsFinished(Pose pose) {
        // if we finish for real, our lookahead is past the goal and our robot has crossed the finish line
        if (lastLookAheadIndex > path.goalComponentIndex) {
            System.out.println("asjdlksad");
        }
        isFinished = (isFinished ||
            path.mainPathPoints.isEmpty() ||
                (lastLookAheadIndex > path.goalComponentIndex && path.isPastFinishLine(pose.getPoint()))
        );
        return isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
