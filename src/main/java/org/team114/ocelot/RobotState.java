package org.team114.ocelot;

import org.team114.ocelot.util.Pose;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RobotState {
    @Inject
    RobotState() {}

    private Pose latest;

    public synchronized void addObservation(Pose pose) {
        latest = pose;
    }

    public synchronized Pose getPose() {
        return latest;
    }
}
