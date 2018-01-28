package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;
import org.team114.lib.geometry.Point;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PathComponent;
import org.team114.ocelot.util.motion.PathPointList;
import org.team114.ocelot.util.motion.PurePursuitController;
import org.team114.ocelot.SubsystemSingletons;
import org.team114.ocelot.util.motion.PurePursuitFactory;

import java.util.Arrays;

public class FollowPathTestAction implements Action {
    PurePursuitController controller;
    PathPointList path = new PathPointList(Arrays.asList(new PathComponent(new Point(5, 0), 0)));


    @Override
    public boolean finished() {
        return controller.isFinished();
    }

    @Override
    public void start() {
        controller = new PurePursuitController(20, path, 0, 1, Timer.getFPGATimestamp());
    }

    @Override
    public void stop() {
        SubsystemSingletons.drive.setDriveSignal(new DriveSignal(0,0));

    }

    @Override
    public void step() {
        PurePursuitController.DriveArcCommand command = controller.getCommand(RobotState.shared.getLatestPose(), Timer.getFPGATimestamp());
        SubsystemSingletons.drive.setDriveArcCommand(command);
    }
}
