package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.util.Pose;

public class PurePursuitController {


    public static class DriveArcCommand {
        //negative is right, positive is left
        public final double curvature;
        public final double vel;

        public DriveArcCommand(double curvature, double vel) {
            this.curvature = curvature;
            this.vel = vel;
        }
    }
    private final PathPointList path;
    private final double pathLength;

    private final double lookAheadDistance;
    private final double finalVelocity;

    private boolean isFinished = false;
    private int lastLookAheadPoint;
    private Pose lastKnownPose;
    private double lastTimeStamp;
    private double finishMargin;

    public PurePursuitController(double lookAheadDistance, PathPointList path, double finalVelocity, double finishMargin, double timestamp) {
        this.lookAheadDistance = lookAheadDistance;
        this.path = path;
        this.finalVelocity = finalVelocity;
        this.lastLookAheadPoint = 0;
        lastKnownPose = RobotState.shared.getLatestPose(); //TODO implement
        lastTimeStamp = timestamp;
        if (this.path.pathComponentList.size() > 0) {
            pathLength = this.path.get(this.path.pathComponentList.size()-1).getDistance();
        } else {
            pathLength = 0;
        }
    }

    private PathComponent getLookAheadPoint(Pose pose) {
        int search = lastLookAheadPoint;
        while (path.get(search).getLocation().dist(pose.getPoint()) < lookAheadDistance &&
                search < path.pathComponentList.size()-1) {
            search++;
        }
        return path.get(search);
    }

    private Point getPivot(Point objective){
        //account for NaN
        if(objective.x() == lastKnownPose.getX() && objective.y() == lastKnownPose.getY())
            return lastKnownPose.getPoint();

        double m = -1 / Math.tan(lastKnownPose.getHeading());
        double b = m * -lastKnownPose.getX() + lastKnownPose.getY();

        double m2 = (lastKnownPose.getX() - objective.x()) / (objective.y() - lastKnownPose.getY());
        double b2 = (-m2 * (lastKnownPose.getX() + objective.x()) + lastKnownPose.getY() + objective.y()) / 2;

        double pivotX = (b - b2) / (m2 - m);
        return new Point(pivotX, m * pivotX + b);
    }

    private double threePointAngle(Point robot, Point target, double radius) {
        double hyp = target.hyp2(robot);
        double temp = -2 * radius * radius;

        return Math.acos( (hyp+temp)/temp );
    }



    public DriveArcCommand getCommand(Pose pose, double timestamp) {
        PathComponent targetComponent = getLookAheadPoint(pose);
        if (isFinished) {
            return new DriveArcCommand(0,0);
        }
        else if (path.pathComponentList.size() > 0) {
            if (path.pathComponentList.get(path.pathComponentList.size()-1).getLocation().dist(pose.getPoint()) < finishMargin) {
                isFinished = true;
                return new DriveArcCommand(0,0);
            }

        } else {
            isFinished = true;
            return new DriveArcCommand(0,0);
        }
        Point targetPoint = targetComponent.getLocation();
        Point arcCenter = getPivot(targetPoint);
        double arcRadius = arcCenter.dist(pose.getPoint());

        double angleToObjective = Math.atan2(targetPoint.y() - arcCenter.y(), targetPoint.x() - arcCenter.x());

        double arcLength = threePointAngle(lastKnownPose.getPoint(), targetPoint, arcRadius) * arcRadius;

        //MUST FIX
        double targetVelocity = 0;
        //double targetVelocity = MotionProfile.getVelocity(timestamp-lastTimeStamp, 0, arcLength + pathLength
        //        - targetComponent.getDistance(), lastKnownPose.getVel(), finalVelocity);


        double signOfCurvature = Math.signum(Math.atan2(targetPoint.y() - pose.getPoint().y(), targetPoint.x() - pose.getPoint().x()) - pose.getHeading());

        lastKnownPose = pose;
        lastTimeStamp = timestamp;

        return new DriveArcCommand(signOfCurvature/arcRadius, targetVelocity);
    }

    public boolean isFinished() {
        return isFinished;
    }
}
