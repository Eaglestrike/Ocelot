package org.team114.ocelot.auto.modes;

import openrio.powerup.MatchData;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.*;
import org.team114.ocelot.auto.gen.StartingPoses;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.subsystems.superstructure.CarriageElevationStage;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PurePursuitFactory;

import javax.inject.Inject;

public class MiddleToSwitch extends AutoModeBase {

    private final Drive drive;
    private final Superstructure sstruct;
    private final RobotState rstate;

    @Inject
    public MiddleToSwitch(Drive drive, Superstructure sstruct, RobotState rstate) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.rstate = rstate;
    }

    @Override
    protected void routine() {
        runAction(new SetKnownStateAction(drive, sstruct, StartingPoses.centerStart, CarriageElevationStage.RAISED, Superstructure.State.StateEnum.CLOSED));
        runAction(new ZeroLiftOneShotAction(sstruct));
        runAction(new WaitAction(0.2));

        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new MoveLiftOneShotAction(sstruct, Settings.SuperStructure.AUTO_SWITCH_HEIGHT_TICKS));
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("centerToLeftSwitch"), 2));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new MoveLiftOneShotAction(sstruct, Settings.SuperStructure.AUTO_SWITCH_HEIGHT_TICKS));
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("centerToRightSwitch"), 2));
        } else {
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
            runAction(new WaitAction(3));
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
            return;
        }
        runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.MIDDLE));
        runAction(new WaitAction(0.5));
        runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, Settings.Carriage.OUTTAKE_COMMAND_NORMAL));
    }
}
