package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

public class RobotState {

    private volatile Pose latest;

    public void addObservation(Pose pose) {
        latest = pose;
    }

    public Pose getPose() {
        return latest;
    }
}
