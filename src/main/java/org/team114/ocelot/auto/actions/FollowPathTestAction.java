package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;
import org.team114.lib.geometry.Point;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathComponent;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitController;
import org.team114.ocelot.Subsystems;

import java.util.Arrays;

public class FollowPathTestAction implements Action {
    private Subsystems subsystems;
    private PurePursuitController controller;

    public FollowPathTestAction(Subsystems subsystems, PathPointList path, double lookAheadDistance, double finishMargin) {
        this.subsystems = subsystems;
        this.controller = new PurePursuitController(lookAheadDistance, path, finishMargin);
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
        subsystems.getDrive().setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        PurePursuitController.DriveArcCommand command = controller.getCommand(RobotState.shared.getLatestPose(), Timer.getFPGATimestamp());
        subsystems.getDrive().setDriveArcCommand(command);
    }
}
