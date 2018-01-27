package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.SubsystemSingletons;
import org.team114.ocelot.util.DriveSignal;

public class DriveCommandAction implements Action {
    DriveSignal signal;

    public DriveCommandAction(DriveSignal d) {
        this.signal = d;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void start() {
        SubsystemSingletons.drive.setDriveSignal(signal);
    }

    @Override
    public void stop() {
        SubsystemSingletons.drive.setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        SubsystemSingletons.drive.setDriveSignal(signal);
    }
}
