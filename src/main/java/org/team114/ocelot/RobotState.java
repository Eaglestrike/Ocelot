package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

public class RobotState {

    private final RobotRegistry robotRegistry;
    private Pose latest;

    public RobotState(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
    }

    public synchronized void addObservation(Pose pose) {
        this.latest = pose;
    }

    public synchronized Pose getLatestPose() {
        return this.latest;
    }
}
