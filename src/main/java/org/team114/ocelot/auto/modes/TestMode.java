package org.team114.ocelot.auto.modes;

import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.PurePursuitAction;
import org.team114.ocelot.auto.actions.ZeroLiftAndBlockAction;
import org.team114.ocelot.auto.actions.ZeroLiftOneShotAction;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.util.motion.PurePursuitFactory;

import javax.inject.Inject;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    private final Drive drive;
    private final RobotState robotState;
    private final Superstructure sstruct;

    @Inject
    public TestMode(Drive drive, Superstructure sstruct, RobotState robotState) {
        this.drive = drive;
        this.sstruct = sstruct;
        this.robotState = robotState;

    }

    @Override
    protected void routine() {
        runAction(new ZeroLiftAndBlockAction(sstruct));
        runAction(new PurePursuitAction(drive, robotState,
                PurePursuitFactory.loadPath("jamesFigureEight"), 2));
    }
}
