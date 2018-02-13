package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.subsystems.DriveInterface;
import org.team114.ocelot.util.DriveSignal;

public class DriveCommandAction implements Action {
    private final DriveInterface drive;
    private final DriveSignal signal;

    public DriveCommandAction(DriveInterface drive, DriveSignal signal) {
        this.drive = drive;
        this.signal = signal;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void start() {
        drive.setDriveSignal(signal);
    }

    @Override
    public void stop() {
        // for testing /validation
        drive.setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        drive.setDriveSignal(signal);
    }
}
