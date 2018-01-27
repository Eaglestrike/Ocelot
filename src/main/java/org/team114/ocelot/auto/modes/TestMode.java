package org.team114.ocelot.auto.modes;

import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.DriveCommandAction;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    @Override
    protected void routine() {
        MatchData.OwnedSide side = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        if (side == MatchData.OwnedSide.LEFT) {
            runAction(new DriveCommandAction(new DriveSignal(0.5, 0.5)));
        } else if (side == MatchData.OwnedSide.RIGHT) {
            runAction(new DriveCommandAction(new DriveSignal(-0.5, -0.5)));
        } else {
            runAction(new DriveCommandAction(new DriveSignal(-0.5, 0.5)));
        }
    }
}
