package org.team114.robot2018.subsystems;

import org.team114.lib.subsystem.Subsystem;

public class RobotState implements Subsystem {

    public class Pose {
        double x;
        double y;
        double radians;

        public Pose(double x, double y, double radians) {
            this.x = x;
            this.y = y;
            this.radians = radians;
        }

        double angle() {
            return this.radians;
        }

        double x() {
            return this.x;
        }

        double y() {
            return this.y;
        }
    }

    Pose currentPose = null;

    public RobotState() {
    }

    @Override
    public void onStart(double timestamp) {
        currentPose = new Pose(0, 0, 0);
    }

    @Override
    public void onStop(double timestamp) {

    }

    @Override
    public void onStep(double timestamp) {

    }
}
