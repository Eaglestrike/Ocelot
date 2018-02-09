package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.Registry;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitController;
import org.team114.ocelot.util.motion.PurePursuitFactory;

public class FollowPathTestAction implements Action {
    private Registry registry;
    private PurePursuitController controller;

    public FollowPathTestAction(Registry registry, PathPointList path, double lookAheadDistance, double finishMargin) {
        this.registry = registry;
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
        registry.get(AbstractDrive.class).setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        PurePursuitController.DriveArcCommand command = controller.getCommand(
                this.registry.get(RobotState.class).getPose(),
                Timer.getFPGATimestamp());
        registry.get(AbstractDrive.class).setDriveArcCommand(command);
    }
}
