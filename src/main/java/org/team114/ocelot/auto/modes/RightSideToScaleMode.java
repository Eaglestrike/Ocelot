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

public class RightSideToScaleMode extends AutoModeBase {

    private final Drive drive;
    private final Superstructure sstruct;
    private final RobotState rstate;

    @Inject
    public RightSideToScaleMode(Drive drive, Superstructure sstruct, RobotState rstate) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.rstate = rstate;
    }

    @Override
    protected void routine() {
        runAction(new SetKnownStateAction(drive, sstruct, StartingPoses.rightSideStart, CarriageElevationStage.RAISED, Superstructure.State.StateEnum.CLOSED));
        runAction(new ZeroLiftOneShotAction(sstruct));

        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        if (side == MatchData.OwnedSide.LEFT) {
            System.out.println("Running path to left scale");
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("rightToLeftScale"), 2));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            System.out.println("Running path to right scale");
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("rightToRightScale"), 2));
        } else {            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
            runAction(new WaitAction(3));
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
            return;
        }
        runAction(new MoveLiftAction(sstruct, Settings.SuperStructure.AUT0_SCALE_HEIGHT_TICKS));
        System.out.println("FINISHED MOVING THE LIFT");
        runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.MIDDLE));
        runAction(new WaitAction(0.7));
        runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, Settings.Carriage.OUTTAKE_COMMAND_NORMAL));
    }
}
