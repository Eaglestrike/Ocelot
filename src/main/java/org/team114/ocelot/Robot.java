package org.team114.ocelot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DashboardHandle;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.PercentageRange;
import org.team114.ocelot.util.Side;
import edu.wpi.first.wpilibj.Timer;

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

    DashboardHandle climbingCountdown = new DashboardHandle("Climbing Countdown");
    DashboardHandle pneumaticPressure = new DashboardHandle("Pneumatic Pressure");

    private SubsystemManager subsystemManager;
    private AbstractDrive drive;

    private AutoModeExecutor autoModeExecutor;

    private RobotRegistryImpl robotRegistry;


    private GearShifter gearShifter;
    private PneumaticPressureSensor pressureSensor;

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
        }
        this.robotRegistry = new RobotRegistryImpl(robotSettings);
        this.robotRegistry.put(new RobotState(this.robotRegistry));
        RobotSide leftSide = new RobotSide(robotRegistry.getSubRobotRegistry("RobotSide.left"));
        robotRegistry.put(ROBOT_SIDE_LEFT, leftSide);
        RobotSide rightSide = new RobotSide(robotRegistry.getSubRobotRegistry("RobotSide.right"));
        robotRegistry.put(ROBOT_SIDE_RIGHT, rightSide);

        robotRegistry.put(new CheesyDriveHelper(robotRegistry.getSubRobotRegistry("CheesyDriveHelper")));

        robotRegistry.put(new Gyro(robotRegistry.getSubRobotRegistry("Gyro")));

        robotRegistry.put(Robot.DB_xPositionDB, new DashboardHandle("Pose X"));
        robotRegistry.put(Robot.DB_yPositionDB, new DashboardHandle("Pose Y"));
        robotRegistry.put(Robot.DB_headingDB, new DashboardHandle("Pose hdg"));
        robotRegistry.put(Robot.DB_velocityDB, new DashboardHandle("Pose vel"));

        Controller controller = new DualController(new Joystick(0), new Joystick(1));
        robotRegistry.put(Controller.class, controller);
        this.gearShifter = new GearShifter(this.robotRegistry.getSubRobotRegistry("GearShifter"));

        drive = new Drive(robotRegistry.getSubRobotRegistry("Drive"));
        robotRegistry.put(AbstractDrive.class, drive);

        subsystemManager = new SubsystemManager(robotRegistry.getSubRobotRegistry("SubsystemManager"),
                Arrays.asList(drive));
        subsystemManager.start();

        climbingCountdown.put(RobotSettings.CLIMBING_TIME);
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(RobotSettings.PNEUMATIC_PRESSURE_SENSOR_ID));
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
        double timeLeft = Math.round(RobotSettings.GAME_TIME - Timer.getMatchTime() - RobotSettings.CLIMBING_TIME);
        climbingCountdown.put(timeLeft);
        pneumaticPressure.put(pressureSensor.getPressure());
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
        PercentageRange throttle = controller.throttle();
        PercentageRange wheel = controller.wheel();
        CheesyDriveHelper cheesyDriveHelper = robotRegistry.get(CheesyDriveHelper.class);
        DriveSignal signal = cheesyDriveHelper.cheesyDrive(throttle, wheel, controller.quickTurn());
        return signal;
    }
}
