package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitController;
import org.team114.ocelot.util.motion.PurePursuitFactory;

public class FollowPathTestAction implements Action {
    private RobotRegistry robotRegistry;
    private PurePursuitController controller;

    public FollowPathTestAction(RobotRegistry robotRegistry, PathPointList path, double lookAheadDistance, double finishMargin) {
        this.robotRegistry = robotRegistry;
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
        robotRegistry.get(AbstractDrive.class).setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        PurePursuitController.DriveArcCommand command = controller.getCommand(
                this.robotRegistry.get(RobotState.class).getLatestPose(),
                Timer.getFPGATimestamp());
        robotRegistry.get(AbstractDrive.class).setDriveArcCommand(command);
    }
}
