package org.team114.ocelot.auto.modes;

import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.auto.actions.FollowPathTestAction;
import org.team114.ocelot.subsystems.DriveInterface;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitFactory;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    private final DriveInterface drive;
    private final RobotState robotState;

    public TestMode(DriveInterface drive, RobotState robotState) {
        this.drive = drive;
        this.robotState = robotState;
    }

    @Override
    protected void routine() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new DriveCommandAction(drive, new DriveSignal(0.5, 0.5)));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new DriveCommandAction(drive, new DriveSignal(-0.5, -0.5)));
        } else {
            PathPointList path = PurePursuitFactory.loadPath("circle");
//            PathPointList path = new PathPointList(Arrays.asList(new PathComponent(new Point(5.0, 5.0), 5)));

            runAction(new FollowPathTestAction(
                    path, drive, robotState
                    , // lookahead distance
                    // finish margin
                    5, 0.8));
        }
    }
}
