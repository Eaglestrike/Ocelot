package org.team114.ocelot.auto.actions;

import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.motion.PurePursuitController;

public class DriveArcAction extends OneShotAction {
    Drive drive;
    PurePursuitController.DriveArcCommand command;

    public DriveArcAction(Drive drive, PurePursuitController.DriveArcCommand command) {
        this.drive = drive;
        this.command = command;
    }

    @Override
    public void start() {
        drive.setDriveArcCommand(command);
    }
}
