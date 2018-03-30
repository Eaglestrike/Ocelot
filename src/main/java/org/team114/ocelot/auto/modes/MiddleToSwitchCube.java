package org.team114.ocelot.auto.modes;

import openrio.powerup.MatchData;
import org.team114.ocelot.Robot;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.*;
import org.team114.ocelot.modules.CarriageElevationStage;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitFactory;

public class MiddleToSwitchCube extends AutoModeBase {

    Drive drive;
    Superstructure sstruct;
    RobotState rstate;

    public MiddleToSwitchCube(Drive drive, Superstructure sstruct, RobotState rstate) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.rstate = rstate;
    }

    @Override
    protected void routine() {
        runAction(new ZeroLiftOneShotAction(sstruct));
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
//        if (side == MatchData.OwnedSide.LEFT) {
//            runAction(new PurePursuitAction(drive, rstate,
//                    PurePursuitFactory.loadPath("middleToLeftSwitch"), 2));
//        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("middleToRightSwitch"), 2));
//        } else {
//             WHAT - cross baseline
//            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
//            runAction(new WaitAction(3));
//            runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
//        }
        runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.LOWERED));
        runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, 0.5));
    }
}
