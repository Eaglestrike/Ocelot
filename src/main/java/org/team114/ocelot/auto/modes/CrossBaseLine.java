package org.team114.ocelot.auto.modes;

import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.SetDriveCommandAction;
import org.team114.ocelot.auto.actions.WaitAction;
import org.team114.ocelot.auto.actions.ZeroLiftOneShotAction;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.DriveSignal;

public class CrossBaseLine extends AutoModeBase {
    Drive drive;
    Superstructure sstruct;

    public CrossBaseLine(Drive drive, Superstructure sstruct) {
        this.drive = drive;
        this.sstruct = sstruct;
    }

    @Override
    protected void routine() {
        runAction(new ZeroLiftOneShotAction(sstruct));
        runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
        runAction(new WaitAction(2));
        runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
    }
}
