package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;

public class SetDriveCommandAction extends OneShotAction {
    private final Drive drive;
    private final DriveSignal signal;

    public SetDriveCommandAction(Drive drive, DriveSignal signal) {
        this.drive = drive;
        this.signal = signal;
    }

    @Override
    public void start() {
        drive.setDriveSignal(signal);
    }
}
