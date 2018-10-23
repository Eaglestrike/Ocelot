package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.subsystems.superstructure.CarriageElevationStage;
import org.team114.ocelot.util.Pose;

import javax.inject.Inject;

public class SetKnownStateAction extends OneShotAction{
    private Drive drive;
    private Pose pose;
    private CarriageElevationStage stage;
    private Superstructure sstruct;
    private Superstructure.State.StateEnum state;

    public SetKnownStateAction(Drive drive, Superstructure sstruct, Pose pose, CarriageElevationStage stage, Superstructure.State.StateEnum state) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.pose = pose;
        this.stage = stage;
        this.state = state;
    }

    @Override
    public void start() {
        this.drive.zeroAllSensors();
        this.drive.setPose(this.pose);
        this.sstruct.actuateIntakeLift(stage);
        this.sstruct.setState(state);
    }
}
