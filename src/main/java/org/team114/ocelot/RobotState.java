package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RobotState {
    @Inject
    RobotState() {}

    private volatile Pose latest;

    public void addObservation(Pose pose) {
        latest = pose;
    }

    public Pose getPose() {
        return latest;
    }
}
