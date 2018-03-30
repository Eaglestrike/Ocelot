package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Drive;

public class ShiftGearAction extends OneShotAction {
    public ShiftGearAction(Drive drive, Drive.State state) {
        this.drive = drive;
        this.state = state;
    }

    Drive.State state;
    Drive drive;


    @Override
    public void start() {
        drive.setGear(state);
    }
}
