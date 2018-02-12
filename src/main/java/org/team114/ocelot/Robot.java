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
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.lib.util.DashboardHandle;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    public static final String countdownDB = "Climbing Countdown";
    public static final String pneumaticPressureDB = "Pneumatic Pressure";
    public static final String gearDB = "Gear";

    private RegistryImpl registry;
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;

    private PneumaticPressureSensor pressureSensor;

    private Controller controller;
    private CheesyDriveHelper cheesyDrive;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {

        registry = new RegistryImpl();
        autoModeExecutor = new AutoModeExecutor();
        RobotState robotState = new RobotState();

        // create modules
        Gyro gyro = Gyro.shared;
        GearShifter gearShifter = new GearShifter();
        DriveSide leftSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.LEFT_MASTER),
                new TalonSRX(Settings.DriveSide.LEFT_SLAVE));
        DriveSide rightSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.RIGHT_MASTER),
                new TalonSRX(Settings.DriveSide.RIGHT_SLAVE));

        // create subsystems
        AbstractDrive drive = new Drive(
                registry,
                leftSide,
                rightSide);

        // register general stuff
        registry.put(robotState);

        // register handles
        registry.put(Robot.countdownDB, new DashboardHandle(Robot.countdownDB));
        registry.put(Robot.pneumaticPressureDB, new DashboardHandle(Robot.pneumaticPressureDB));
        registry.put(Robot.gearDB, new DashboardHandle(Robot.gearDB));

        // register modules
        registry.put(gyro);
        registry.put(gearShifter);

        // register subsystems
        registry.put(AbstractDrive.class, drive);

        // create & kick off subsystem manager
        subsystemManager = new SubsystemManager(
            drive
        );
        subsystemManager.start();

        // driver facing stuff
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(Settings.PNEUMATIC_PRESSURE_SENSOR_ID));
        cheesyDrive = new CheesyDriveHelper();
        controller = new DualController(new Joystick(0), new Joystick(1));
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
        double timeLeft = Math.round(Settings.GAME_TIME - Timer.getMatchTime() - Settings.CLIMBING_TIME_ESTIMATE);
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
        drive.setDriveSignal(cheesyDrive.cheesyDrive(controller.throttle(), controller.wheel(), controller.quickTurn()));
        drive.setGear(controller.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);
    }

    @Override
    public void testPeriodic() {
    }
}
