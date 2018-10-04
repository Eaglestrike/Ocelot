package org.team114.ocelot.auto.modes;

import openrio.powerup.MatchData;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.*;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.subsystems.superstructure.CarriageElevationStage;
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
        runAction(new ZeroLiftOneShotAction(sstruct));
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("crossAutoLine"), 2));
            return;
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("rightToRightScale"), 2));
        } else {
            runAction(new PurePursuitAction(drive, rstate,
                    PurePursuitFactory.loadPath("crossAutoLine"), 2));
            return;
        }
        runAction(new MoveLiftAction(sstruct, Settings.SuperStructure.AUT0_SCALE_HEIGHT_TICKS));
        runAction(new ElevateIntakeOneShotAction(sstruct, CarriageElevationStage.MIDDLE));
        runAction(new WaitAction(0.5));
        runAction(new TriggerIntakeOneShotAction(sstruct, Superstructure.State.StateEnum.OUTTAKING, Settings.Carriage.OUTTAKE_COMMAND_NORMAL));
    }
}
