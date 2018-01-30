package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.Controller;
import org.team114.ocelot.modules.DualController;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DashboardHandle;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Side;

import java.io.IOException;
import java.util.Arrays;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    public static final String ROBOT_SIDE_LEFT = "robotSide.left";
    public static final String ROBOT_SIDE_RIGHT = "robotSide.right";
    public static final String DB_xPositionDB = "Pose X";
    public static final String DB_yPositionDB = "Pose Y";
    public static final String DB_headingDB = "Pose hdg";
    public static final String DB_velocityDB = "Pose vel";

    SubsystemManager subsystemManager;
    AbstractDrive drive;

    AutoModeExecutor autoModeExecutor;

    private RobotRegistryImpl robotRegistry;

    @Override
    public void robotInit() {
        RobotSettings robotSettings = new RobotSettings();
        try {
            robotSettings.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.robotRegistry = new RobotRegistryImpl(robotSettings, null);

        RobotSide leftSide = new RobotSide(robotRegistry.getRobotRegistry("RobotSide.left"));
        robotRegistry.put(ROBOT_SIDE_LEFT, leftSide);
        RobotSide rightSide = new RobotSide(robotRegistry.getRobotRegistry("RobotSide.right"));
        robotRegistry.put(ROBOT_SIDE_RIGHT, rightSide);

        robotRegistry.put(new CheesyDriveHelper(robotRegistry.getRobotRegistry("CheesyDriveHelper")));

        robotRegistry.put(new Gyro(robotRegistry.getRobotRegistry("Gyro")));

        robotRegistry.put(Robot.DB_xPositionDB, new DashboardHandle("Pose X"));
        robotRegistry.put(Robot.DB_yPositionDB, new DashboardHandle("Pose Y"));
        robotRegistry.put(Robot.DB_headingDB, new DashboardHandle("Pose hdg"));
        robotRegistry.put(Robot.DB_velocityDB, new DashboardHandle("Pose vel"));

        Controller controller = new DualController(new Joystick(0), new Joystick(1));
        robotRegistry.put(Controller.class, controller);
        drive = new Drive(robotRegistry.getRobotRegistry("Drive"));
        robotRegistry.put(drive);

        subsystemManager = new SubsystemManager(robotRegistry.getRobotRegistry("SubsystemManager"),
                Arrays.asList(drive));
        subsystemManager.start();
    }

    @Override
    public void disabledInit() {
        if (autoModeExecutor != null) {
            autoModeExecutor.stop();
        }
        autoModeExecutor = null;
    }

    @Override
    public void autonomousInit() {
        autoModeExecutor = new AutoModeExecutor();
        autoModeExecutor.setAutoMode(new TestMode(this.robotRegistry));
        autoModeExecutor.start();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        DriveSignal signal = getDriveSignal();
        drive.setSideSpeed(Side.LEFT, -signal.getLeft());
        drive.setSideSpeed(Side.RIGHT, signal.getRight());
    }

    @Override
    public void testPeriodic() {
    }

    private Controller getController() {
        return this.robotRegistry.get(Controller.class);
    }
    private DriveSignal getDriveSignal() {
        Controller controller = getController();
        double throttle = controller.throttle();
        double wheel = controller.wheel();
        CheesyDriveHelper cheesyDriveHelper = robotRegistry.get(CheesyDriveHelper.class);
        DriveSignal signal = cheesyDriveHelper.cheesyDrive(throttle, -wheel, controller.quickTurn());
        return signal;
    }
}
