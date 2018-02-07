package org.team114.ocelot.auto.modes;

import org.team114.ocelot.Registry;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.auto.actions.FollowPathTestAction;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitFactory;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    public TestMode(Registry registry) {
        super(registry);
    }

    @Override
    protected void routine() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new DriveCommandAction(registry, new DriveSignal(0.5, 0.5)));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new DriveCommandAction(registry, new DriveSignal(-0.5, -0.5)));
        } else {
            PathPointList path = PurePursuitFactory.loadPath("circle");

            runAction(new FollowPathTestAction(
                    registry, path,
                20, // lookahead distance
                1 // finish margin
            ));
        }
    }
}
