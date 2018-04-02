package org.team114.ocelot.auto.modes;

import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveArcAction;
import org.team114.ocelot.auto.actions.ShiftGearAction;
import org.team114.ocelot.auto.actions.WaitAction;
import org.team114.ocelot.auto.actions.ZeroLiftOneShotAction;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.motion.PurePursuitController;

public class TestArcMode extends AutoModeBase {
    private final Drive drive;
    private final Superstructure sstruct;

    public TestArcMode(Drive drive, Superstructure sstruct) {
        this.drive = drive;
        this.sstruct = sstruct;
    }

    @Override
    protected void routine() {
        runAction(new ShiftGearAction(drive, Drive.State.LOW));
        runAction(new ZeroLiftOneShotAction(sstruct));
        runAction(new DriveArcAction(drive, new PurePursuitController.DriveArcCommand(0.2, 4)));
        runAction(new WaitAction(2));
        runAction(new DriveArcAction(drive, new PurePursuitController.DriveArcCommand(0, 0)));
    }
}
