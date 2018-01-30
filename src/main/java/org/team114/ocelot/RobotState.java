package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RobotState {
    public final static RobotState shared = new RobotState();

    private Pose latest;

    @Inject public RobotState() {}

    public synchronized void addObservation(Pose pose) {
        this.latest = pose;
    }

    public synchronized Pose getLatestPose() {
        return this.latest;
    }
}
