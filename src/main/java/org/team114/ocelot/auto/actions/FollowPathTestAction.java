package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitController;
import org.team114.ocelot.util.motion.PurePursuitFactory;

public class FollowPathTestAction implements Action {
    private final Drive drive;
    private final RobotState robotState;
    private final PurePursuitController controller;

    public FollowPathTestAction(Drive drive, RobotState robotState, PathPointList path,
                                double lookAheadDistance, double finishMargin) {
        this.drive = drive;
        this.robotState = robotState;
        this.controller = PurePursuitFactory.startPurePursuit(path, lookAheadDistance, finishMargin);
    }

    @Override
    public boolean finished() {
        return controller.isFinished();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        drive.setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        PurePursuitController.DriveArcCommand command = controller.getCommand(
                robotState.getPose(),
                Timer.getFPGATimestamp());
        drive.setDriveArcCommand(command);
    }
}
