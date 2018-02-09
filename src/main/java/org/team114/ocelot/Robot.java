package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.lib.util.DashboardHandle;
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
    public static final String pneumaticPressureDB = "Pneumatic Pressure";
    public static final String gearDB = "Gear";

    private RobotRegistryImpl robotRegistry;
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;

    private PneumaticPressureSensor pressureSensor;

    private Controller driverControls;
    private CheesyDriveHelper cheesyDrive;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
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
        RobotState robotState = new RobotState();

        // create modules
        Gyro gyro = Gyro.shared;
        GearShifter gearShifter = new GearShifter(robotRegistry.getSubRobotRegistry("GearShifter"));

        // create subsystems
        AbstractDrive drive = new Drive(robotRegistry.getSubRobotRegistry("Drive"),
                new TalonSRX(RobotSettings.LEFT_MASTER_ID), new TalonSRX(RobotSettings.LEFT_SLAVE_ID),
                new TalonSRX(RobotSettings.RIGHT_MASTER_ID), new TalonSRX(RobotSettings.RIGHT_SLAVE_ID));

        // register general stuff
        robotRegistry.put(robotState);

        // register handles
        robotRegistry.put(Robot.xPositionDB, new DashboardHandle(Robot.xPositionDB));
        robotRegistry.put(Robot.yPositionDB, new DashboardHandle(Robot.yPositionDB));
        robotRegistry.put(Robot.headingDB, new DashboardHandle(Robot.headingDB));
        robotRegistry.put(Robot.velocityDB, new DashboardHandle(Robot.velocityDB));
        robotRegistry.put(Robot.countdownDB, new DashboardHandle(Robot.countdownDB));
        robotRegistry.put(Robot.pneumaticPressureDB, new DashboardHandle(Robot.pneumaticPressureDB));
        robotRegistry.put(Robot.gearDB, new DashboardHandle(Robot.gearDB));

        // register modules
        robotRegistry.put(gyro);
        robotRegistry.put(gearShifter);

        // register subsystems
        robotRegistry.put(AbstractDrive.class, drive);

        // create & kick off subsystem manager
        subsystemManager = new SubsystemManager(
            drive
        );
        subsystemManager.start();

        // driver facing stuff
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(RobotSettings.PNEUMATIC_PRESSURE_SENSOR_ID));
        cheesyDrive = new CheesyDriveHelper(robotRegistry.getSubRobotRegistry("CheesyDriveHelper"));
        driverControls= new DualController(new Joystick(0), new Joystick(1));

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
        DashboardHandle pneumaticPressureHandle = robotRegistry.get(pneumaticPressureDB);
        pneumaticPressureHandle.put(pressureSensor.getPressure());

        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(RobotSettings.GAME_TIME - Timer.getMatchTime() - RobotSettings.CLIMBING_TIME);
        DashboardHandle countdownHandle = robotRegistry.get(countdownDB);
        countdownHandle.put(timeLeft);

        GearShifter gearShifter = robotRegistry.get(GearShifter.class);
        DashboardHandle gearHandle = robotRegistry.get(gearDB);
        switch (gearShifter.get()) {
            case HIGH:
                gearHandle.put(true);
                break;
            case LOW:
                gearHandle.put(false);
                break;
        }
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
        GearShifter gearShifter = robotRegistry.get(GearShifter.class);

        drive.setDriveSignal(cheesyDrive.cheesyDrive(driverControls.throttle(), driverControls.wheel(), driverControls.quickTurn()));
        drive.setGear(driverControls.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);
    }

    @Override
    public void testPeriodic() {
    }
}
