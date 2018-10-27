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

public class LeftSideOnlyMode extends AutoModeBase {

    private final Drive drive;
    private final Superstructure sstruct;
    private final RobotState rstate;

    @Inject
    public LeftSideOnlyMode(Drive drive, Superstructure sstruct, RobotState rstate) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.rstate = rstate;
    }

    @Override
    protected void routine() {
        runAction(new SetKnownStateAction(drive, sstruct, StartingPoses.leftSideStart, CarriageElevationStage.RAISED, Superstructure.State.StateEnum.CLOSED));
        runAction(new ZeroLiftOneShotAction(sstruct));
        runAction(new WaitAction(0.5));


        MatchData.OwnedSide scale = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        MatchData.OwnedSide switch_ = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

        if (scale == MatchData.OwnedSide.LEFT) { // near side scale
            System.out.println("Running path to left scale");
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("leftToLeftScale"), 2));
            System.out.println("PATH FINISHED");
            runAction(new MoveLiftAction(sstruct, Settings.SuperStructure.AUT0_SCALE_HEIGHT_TICKS));
            System.out.println("FINISHED MOVING THE LIFT");
            runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.MIDDLE));
            runAction(new WaitAction(0.7));
            runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, Settings.Carriage.OUTTAKE_COMMAND_NORMAL));
            runAction(new WaitAction(0.7));
            return;
        } else if (switch_ == MatchData.OwnedSide.LEFT) { // near switch
            runAction(new MoveLiftOneShotAction(sstruct, Settings.SuperStructure.AUTO_SWITCH_HEIGHT_TICKS));
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("leftToLeftSwitch"), 2));
            runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.MIDDLE));
            runAction(new WaitAction(0.5));
            runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, Settings.Carriage.OUTTAKE_COMMAND_NORMAL));
            runAction(new WaitAction(0.7));
            return;
        } else { // cross baseline
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
            runAction(new WaitAction(2.9));
            runAction(new SetDriveCommandAction(drive, new DriveSignal(0, 0)));
            return;
        }
    }
}
