package org.team114.ocelot.auto.modes;

import org.team114.lib.geometry.Point;
import org.team114.ocelot.Subsystems;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.auto.actions.FollowPathTestAction;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;
import org.team114.ocelot.util.motion.PathComponent;
import org.team114.ocelot.util.motion.PathPointList;

import java.util.Arrays;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    public TestMode(Subsystems subsystems) {
        super(subsystems);
    }

    @Override
    protected void routine() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new DriveCommandAction(subsystems, new DriveSignal(0.5, 0.5)));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new DriveCommandAction(subsystems, new DriveSignal(-0.5, -0.5)));
        } else {
            PathPointList path = new PathPointList(Arrays.asList(
                new PathComponent(new Point(5, 5), 0)
            ));

            runAction(new FollowPathTestAction(
                subsystems, path,
                20, // lookahead distance
                1 // finish margin
            ));
        }
    }
}
