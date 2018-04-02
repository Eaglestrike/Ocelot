package org.team114.ocelot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import openrio.powerup.MatchData;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.*;
import org.team114.ocelot.modules.CarriageElevationStage;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PurePursuitFactory;

public class MiddleToSwitchCube extends AutoModeBase {

    private final Drive drive;
    private final Superstructure sstruct;
    private final RobotState rstate;

    public MiddleToSwitchCube(Drive drive, Superstructure sstruct, RobotState rstate) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.rstate = rstate;
    }

    @Override
    protected void routine() {
        runAction(new ZeroLiftOneShotAction(sstruct));
        System.out.println(Timer.getFPGATimestamp() + "1231");
        runAction(new MoveLiftAction(sstruct, 10_000));
        System.out.println(Timer.getFPGATimestamp() + "1231");
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("middleToLeftSwitch"), 2));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runActionTimeout(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("middleToRightSwitch"), 2), 5);
        } else {
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
            runAction(new WaitAction(3));
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
        }
        runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.LOWERED));
        runAction(new WaitAction(0.5));
        runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, 0.75));
    }
}
