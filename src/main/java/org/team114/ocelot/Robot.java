package org.team114.ocelot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.Settings;
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

    public static final String DRIVE_SIDE_LEFT = "DriveSide.left";
    public static final String DRIVE_SIDE_RIGHT = "DriveSide.right";
    public static final String xPositionDB = "Pose X";
    public static final String yPositionDB = "Pose Y";
    public static final String headingDB = "Pose hdg";
    public static final String velocityDB = "Pose vel";
    public static final String countdownDB = "Climbing Countdown";
    public static final String pneumaticPressureDB = "Pneumatic Pressure";
    public static final String gearDB = "Gear";

    private RegistryImpl registry;
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;

    private PneumaticPressureSensor pressureSensor;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {
        Settings settings = new Settings();
        try {
            settings.load();
        } catch (IOException e) {
            e.printStackTrace();
            // if we can't load settings, we want to crash the robot
            throw new IllegalStateException();
        }

        registry = new RegistryImpl(settings);
        autoModeExecutor = new AutoModeExecutor();
        CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper(registry.getSubRegistry("CheesyDriveHelper"));
        RobotState robotState = new RobotState();

        // create modules
        Gyro gyro = Gyro.shared;
        Controller controller = new DualController(new Joystick(0), new Joystick(1));
        GearShifter gearShifter = new GearShifter(registry.getSubRegistry("GearShifter"));
        DriveSide leftSide = new DriveSide(registry.getSubRegistry("DriveSide.left"));
        DriveSide rightSide = new DriveSide(registry.getSubRegistry("DriveSide.right"));

        // create subsystems
        AbstractDrive drive = new Drive(registry.getSubRegistry("Drive"));

        // register general stuff
        registry.put(cheesyDriveHelper);
        registry.put(robotState);

        // register handles
        registry.put(Robot.xPositionDB, new DashboardHandle(Robot.xPositionDB));
        registry.put(Robot.yPositionDB, new DashboardHandle(Robot.yPositionDB));
        registry.put(Robot.headingDB, new DashboardHandle(Robot.headingDB));
        registry.put(Robot.velocityDB, new DashboardHandle(Robot.velocityDB));
        registry.put(Robot.countdownDB, new DashboardHandle(Robot.countdownDB));
        registry.put(Robot.pneumaticPressureDB, new DashboardHandle(Robot.pneumaticPressureDB));
        registry.put(Robot.gearDB, new DashboardHandle(Robot.gearDB));

        // register modules
        registry.put(gyro);
        registry.put(Controller.class, controller);
        registry.put(gearShifter);
        registry.put(DRIVE_SIDE_LEFT, leftSide);
        registry.put(DRIVE_SIDE_RIGHT, rightSide);

        // register subsystems
        registry.put(AbstractDrive.class, drive);

        // create & kick off subsystem manager
        subsystemManager = new SubsystemManager(
            drive
        );
        subsystemManager.start();

        pressureSensor = new PneumaticPressureSensor(new AnalogInput(Settings.PNEUMATIC_PRESSURE_SENSOR_ID));
    }

    @Override
    public void disabledInit() {
        autoModeExecutor.stop();
    }

    @Override
    public void autonomousInit() {
        autoModeExecutor.setAutoMode(new TestMode(registry));
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
        DashboardHandle pneumaticPressureHandle = registry.get(pneumaticPressureDB);
        pneumaticPressureHandle.put(pressureSensor.getPressure());

        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(Settings.GAME_TIME - Timer.getMatchTime() - Settings.CLIMBING_TIME);
        DashboardHandle countdownHandle = registry.get(countdownDB);
        countdownHandle.put(timeLeft);

        GearShifter gearShifter = registry.get(GearShifter.class);
        DashboardHandle gearHandle = registry.get(gearDB);
        switch (gearShifter.get()) {
            case HIGH:
                gearHandle.put(1);
                break;
            case LOW:
                gearHandle.put(-1);
                break;
            case OFF:
                gearHandle.put(0);
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
        Controller controller = registry.get(Controller.class);
        AbstractDrive drive = registry.get(AbstractDrive.class);
        GearShifter gearShifter = registry.get(GearShifter.class);

        drive.setDriveSignal(getDriveSignal());

        if (controller.shiftGear().rising()) {
            gearShifter.shift();
        }
    }

    @Override
    public void testPeriodic() {
    }

    private DriveSignal getDriveSignal() {
        Controller controller = registry.get(Controller.class);
        CheesyDriveHelper cheesyDriveHelper = registry.get(CheesyDriveHelper.class);

        PercentageRange throttle = controller.throttle();
        PercentageRange wheel = controller.wheel();

        return cheesyDriveHelper.cheesyDrive(throttle, wheel, controller.quickTurn());
    }
}
