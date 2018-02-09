package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

import java.util.concurrent.atomic.AtomicReference;

public class RobotState {

    private volatile Pose latest;

    public void updatePose(Pose pose) {
        latest = pose;
    }

    public Pose getPose() {
        return latest;
    }
}
