package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.DriveInterface;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;
import org.team114.ocelot.subsystems.SuperstructureInterface;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.lib.util.DashboardHandle;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    private final DashboardHandle countdownDB = new DashboardHandle("Climbing Countdown");
    private final DashboardHandle pneumaticPressureDB = new DashboardHandle("Pneumatic Pressure");
    private final DashboardHandle gearDB = new DashboardHandle("Gear");

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
        GearShifter gearShifter = new GearShifter(
                new DoubleSolenoid(
                        Settings.GearShifter.HIGH_GEAR_CHANNEL,
                        Settings.GearShifter.LOW_GEAR_CHANNEL));
        DriveSide leftSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.LEFT_MASTER),
                new TalonSRX(Settings.DriveSide.LEFT_SLAVE));
        DriveSide rightSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.RIGHT_MASTER),
                new TalonSRX(Settings.DriveSide.RIGHT_SLAVE));
        Carriage carriage = new Carriage(
                new Solenoid(Settings.Carriage.INTAKE_CHANNEL),
                new Solenoid(Settings.Carriage.LIFT_STAGE_ONE),
                new Solenoid(Settings.Carriage.LIFT_STAGE_TWO),
                new TalonSRX(Settings.Carriage.LEFT_SPINNER),
                new TalonSRX(Settings.Carriage.RIGHT_SPINNER),
                new DistanceSensor(new AnalogInput(Settings.DistanceSensor.CHANNEL)));
        Lift lift = new Lift(
                new TalonSRX(Settings.Lift.MASTER),
                new TalonSRX(Settings.Lift.SLAVE),
                new DigitalInput(Settings.Lift.TOP_LIMIT_SWITCH));

        // create subsystems
        DriveInterface drive = new Drive(
                registry,
                leftSide,
                rightSide);
        SuperstructureInterface superstructure = new Superstructure(
                carriage,
                lift);

        // register general stuff
        registry.put(robotState);

        // register modules
        registry.put(gyro);
        registry.put(gearShifter);

        // register subsystems
        registry.put(DriveInterface.class, drive);

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
        DriveInterface drive = registry.get(DriveInterface.class);
        drive.prepareForAuto();

        autoModeExecutor.setAutoMode(new TestMode(registry));
        autoModeExecutor.start();
    }

    @Override
    public void teleopInit() {
        DriveInterface drive = registry.get(DriveInterface.class);
        drive.prepareForTeleop();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void robotPeriodic() {
        pneumaticPressureDB.put(pressureSensor.getPressure());

        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(Settings.GAME_TIME - Timer.getMatchTime() - Settings.CLIMBING_TIME_ESTIMATE);
        countdownDB.put(timeLeft);

        GearShifter gearShifter = registry.get(GearShifter.class);
        switch (gearShifter.get()) {
            case HIGH:
                gearDB.put(true);
                break;
            case LOW:
                gearDB.put(false);
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
        DriveInterface drive = registry.get(DriveInterface.class);
        drive.setDriveSignal(cheesyDrive.cheesyDrive(controller.throttle(), controller.wheel(), controller.quickTurn()));
        drive.setGear(controller.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);
    }

    @Override
    public void testPeriodic() {
    }
}
