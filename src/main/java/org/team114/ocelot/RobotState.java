package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

public class RobotState {
    public final static RobotState shared = new RobotState();

    private Pose latest;

    public synchronized void addObservation(Pose pose) {
        this.latest = pose;
    }

    public synchronized Pose getLatestPose() {
        return this.latest;
    }
}
