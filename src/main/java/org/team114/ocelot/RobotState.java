package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

import java.util.concurrent.atomic.AtomicReference;

public class RobotState {

    private final RobotRegistry robotRegistry;
    private AtomicReference<Pose> latest;

    public RobotState(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
    }

    public void addObservation(Pose pose) {
        this.latest.set(pose);
    }

    public Pose getLatestPose() {
        return this.latest.get();
    }
}
