package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.Controller;
import org.team114.ocelot.modules.DualController;
import org.team114.ocelot.modules.GearShifter;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DashboardHandle;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.PercentageRange;

import java.io.IOException;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    public static final String ROBOT_SIDE_LEFT = "robotSide.left";
    public static final String ROBOT_SIDE_RIGHT = "robotSide.right";
    public static final String xPositionDB = "Pose X";
    public static final String yPositionDB = "Pose Y";
    public static final String headingDB = "Pose hdg";
    public static final String velocityDB = "Pose vel";
    public static final String countdownDB = "Climbing Countdown";

    private RobotRegistryImpl robotRegistry;
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their reprensetations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {
        RobotSettings robotSettings = new RobotSettings();
        try {
            robotSettings.load();
        } catch (IOException e) {
            e.printStackTrace();
            // if we can't load settings, we want to crash the robot
            throw new IllegalStateException();
        }

        robotRegistry = new RobotRegistryImpl(robotSettings);
        autoModeExecutor = new AutoModeExecutor();
        CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper(robotRegistry.getSubRobotRegistry("CheesyDriveHelper"));
        RobotState robotState = new RobotState();

        // create modules
        Gyro gyro = Gyro.shared;
        Controller controller = new DualController(new Joystick(0), new Joystick(1));
        GearShifter gearShifter = new GearShifter(robotRegistry.getSubRobotRegistry("GearShifter"));
        RobotSide leftSide = new RobotSide(robotRegistry.getSubRobotRegistry("RobotSide.left"));
        RobotSide rightSide = new RobotSide(robotRegistry.getSubRobotRegistry("RobotSide.right"));

        // create subsystems
        AbstractDrive drive = new Drive(robotRegistry.getSubRobotRegistry("Drive"));

        // register general stuff
        robotRegistry.put(cheesyDriveHelper);
        robotRegistry.put(robotState);

        // register handles
        robotRegistry.put(Robot.xPositionDB, new DashboardHandle(Robot.xPositionDB));
        robotRegistry.put(Robot.yPositionDB, new DashboardHandle(Robot.yPositionDB));
        robotRegistry.put(Robot.headingDB, new DashboardHandle(Robot.headingDB));
        robotRegistry.put(Robot.velocityDB, new DashboardHandle(Robot.velocityDB));
        robotRegistry.put(Robot.countdownDB, new DashboardHandle(Robot.countdownDB));

        // register modules
        robotRegistry.put(gyro);
        robotRegistry.put(controller);
        robotRegistry.put(gearShifter);
        robotRegistry.put(ROBOT_SIDE_LEFT, leftSide);
        robotRegistry.put(ROBOT_SIDE_RIGHT, rightSide);

        // register subsystems
        robotRegistry.put(drive);

        // create & kick off subsystem manager
        subsystemManager = new SubsystemManager(
            robotRegistry.getSubRobotRegistry("SubsystemManager"),
            drive
        );
        subsystemManager.start();
    }

    @Override
    public void disabledInit() {
        autoModeExecutor.stop();
    }

    @Override
    public void autonomousInit() {
        autoModeExecutor.setAutoMode(new TestMode(robotRegistry));
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
        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(RobotSettings.GAME_TIME - Timer.getMatchTime() - RobotSettings.CLIMBING_TIME);
        DashboardHandle countdownHandle = robotRegistry.get(countdownDB);
        countdownHandle.put(timeLeft);
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        AbstractDrive drive = robotRegistry.get(AbstractDrive.class);
        drive.setDriveSignal(getDriveSignal());
    }

    @Override
    public void testPeriodic() {
    }

    private DriveSignal getDriveSignal() {
        Controller controller = robotRegistry.get(Controller.class);
        CheesyDriveHelper cheesyDriveHelper = robotRegistry.get(CheesyDriveHelper.class);

        PercentageRange throttle = controller.throttle();
        PercentageRange wheel = controller.wheel();

        return cheesyDriveHelper.cheesyDrive(throttle, wheel, controller.quickTurn());
    }
}
