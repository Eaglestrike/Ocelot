package org.team114.ocelot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.Configuration;
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

    private Controller driverControls;
    private CheesyDriveHelper cheesyDrive;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {
        Configuration configuration;
        try {
            configuration = Configuration.loadFromProperties();
        } catch (IOException e) {
            e.printStackTrace();
            // if we can't load settings, we want to crash the robot
            throw new IllegalStateException();
        }

        registry = new RegistryImpl();
        autoModeExecutor = new AutoModeExecutor();
        RobotState robotState = new RobotState();

        // create modules
        Gyro gyro = Gyro.shared;
        GearShifter gearShifter = new GearShifter(configuration.subConfiguration("GearShifter"));
        DriveSide leftSide = new DriveSide(configuration.subConfiguration("DriveSide.left"));
        DriveSide rightSide = new DriveSide(configuration.subConfiguration("DriveSide.right"));

        // create subsystems
        AbstractDrive drive = new Drive(registry, configuration.subConfiguration("Drive"));

        // register general stuff
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

        // driver facing stuff
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(Settings.PNEUMATIC_PRESSURE_SENSOR_ID));
        cheesyDrive = new CheesyDriveHelper(configuration.subConfiguration("CheesyDriveHelper"));
        driverControls= new DualController(new Joystick(0), new Joystick(1));
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
        AbstractDrive drive = registry.get(AbstractDrive.class);
        GearShifter gearShifter = registry.get(GearShifter.class);

        drive.setDriveSignal(cheesyDrive.cheesyDrive(driverControls.throttle(), driverControls.wheel(), driverControls.quickTurn()));
        gearShifter.set(driverControls.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);
    }

    @Override
    public void testPeriodic() {
    }
}
