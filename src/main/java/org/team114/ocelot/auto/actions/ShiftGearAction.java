package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Drive;

public class ShiftGearAction extends OneShotAction {
    private final Drive.State state;
    private final Drive drive;

    public ShiftGearAction(Drive drive, Drive.State state) {
        this.drive = drive;
        this.state = state;
    }

    @Override
    public void start() {
        drive.setGear(state);
    }
}
