package org.team114.ocelot.auto.modes;

import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.auto.actions.WaitAction;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;

public class CrossBaseLine extends AutoModeBase {
    Drive drive;

    CrossBaseLine(Drive drive) {
        this.drive = drive;
    }

    @Override
    protected void routine() {
        runAction(new DriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
        runAction(new WaitAction(3));
        runAction(new DriveCommandAction(drive, new DriveSignal(0, 0)));
    }
}
