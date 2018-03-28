package org.team114.ocelot.auto.modes;

import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.AutoModeBase;
import org.team114.ocelot.auto.actions.SetDriveCommandAction;
import org.team114.ocelot.auto.actions.PurePursuitAction;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;
import openrio.powerup.MatchData;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitFactory;

// this is just to test the functionality of game sides
public class TestMode extends AutoModeBase {
    private final Drive drive;
    private final RobotState robotState;

    public TestMode(Drive drive, RobotState robotState) {
        this.drive = drive;
        this.robotState = robotState;
    }

    @Override
    protected void routine() {}
}
