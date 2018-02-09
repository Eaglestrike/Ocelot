package org.team114.ocelot.auto.modes;

import org.team114.lib.geometry.Point;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.auto.actions.FollowPathTestAction;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;
import org.team114.ocelot.util.motion.PathComponent;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitFactory;

import java.util.Arrays;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    public TestMode(RobotRegistry robotRegistry) {
        super(robotRegistry);
    }

    @Override
    protected void routine() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new DriveCommandAction(robotRegistry, new DriveSignal(0.5, 0.5)));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new DriveCommandAction(robotRegistry, new DriveSignal(-0.5, -0.5)));
        } else {
            PathPointList path = PurePursuitFactory.loadPath("circle");
//            PathPointList path = new PathPointList(Arrays.asList(new PathComponent(new Point(5.0, 5.0), 5)));

            runAction(new FollowPathTestAction(
                robotRegistry, path,
                5, // lookahead distance
                0.8 // finish margin
            ));
        }
    }
}
