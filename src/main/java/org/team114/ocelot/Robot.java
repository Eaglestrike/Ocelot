package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.*;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.*;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.lib.util.DashboardHandle;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    // handles
    private final DashboardHandle countdownDB = new DashboardHandle("Climbing Countdown");
    private final DashboardHandle pneumaticPressureDB = new DashboardHandle("Pneumatic Pressure");
    private final DashboardHandle gearDB = new DashboardHandle("Gear");

    // general
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;
    private RobotState robotState;

    // subsystems
    private DriveInterface drive;
    private SuperstructureInterface superstructure;
    private PneumaticsInterface pneumatics;

    // modules
    private Gyro gyro;
    private GearShifter gearShifter;
    private DriveSide leftSide;
    private DriveSide rightSide;
    private Carriage carriage;
    private Lift lift;

    // driver-facing
    private Controller controller;
    private CheesyDriveHelper cheesyDrive;
    private PneumaticPressureSensor pressureSensor;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {

        // create driver-facing stuff
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(Settings.PNEUMATIC_PRESSURE_SENSOR_ID));
        cheesyDrive = new CheesyDriveHelper();
        controller = new DualController(new Joystick(0), new Joystick(1), new Joystick(2));

        // create modules
        gyro = Gyro.shared;
        gearShifter = new GearShifter(
                new DoubleSolenoid(
                        Settings.GearShifter.HIGH_GEAR,
                        Settings.GearShifter.LOW_GEAR));
        leftSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.LEFT_MASTER),
                new TalonSRX(Settings.DriveSide.LEFT_SLAVE));
        rightSide = new DriveSide(
                new TalonSRX(Settings.DriveSide.RIGHT_MASTER),
                new TalonSRX(Settings.DriveSide.RIGHT_SLAVE));
        carriage = new Carriage(
                new Solenoid(Settings.Carriage.INTAKE_CHANNEL),
                new Solenoid(Settings.Carriage.LIFT_STAGE_ONE),
                new Solenoid(Settings.Carriage.LIFT_STAGE_TWO),
                new TalonSRX(Settings.Carriage.LEFT_SPINNER),
                new TalonSRX(Settings.Carriage.RIGHT_SPINNER),
                new DistanceSensor(new AnalogInput(Settings.DistanceSensor.CHANNEL)));
        lift = new Lift(
                new TalonSRX(Settings.Lift.MASTER),
                new TalonSRX(Settings.Lift.SLAVE),
                new DigitalInput(Settings.Lift.TOP_LIMIT_SWITCH));

        // create subsystems
        drive = new Drive(
                robotState,
                gyro,
                leftSide,
                rightSide,
                gearShifter);
        superstructure = new Superstructure(
                carriage,
                lift);
        pneumatics = new Pneumatics(
                new Compressor(),
                pressureSensor);

        // create general stuff
        robotState = new RobotState();
        autoModeExecutor = new AutoModeExecutor();
        subsystemManager = new SubsystemManager(
                drive,
                superstructure,
                pneumatics
        );

        // kick off subsystem manager
        subsystemManager.start();
    }

    @Override
    public void disabledInit() {
        autoModeExecutor.stop();
    }

    @Override
    public void autonomousInit() {
        drive.prepareForAuto();

        autoModeExecutor.setAutoMode(new TestMode(drive, robotState));
        autoModeExecutor.start();
    }

    @Override
    public void teleopInit() {
        drive.prepareForTeleop();
    }

    @Override
    public void testInit() {
    }

    @Override
    public void robotPeriodic() {
        pneumaticPressureDB.put(pneumatics.getPressure());

        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(Settings.GAME_TIME - Timer.getMatchTime() - Settings.CLIMBING_TIME_ESTIMATE);
        countdownDB.put(timeLeft);

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


    //Temporary fix
    private double time = 0;

    @Override
    public void teleopPeriodic() {
        drive.setDriveSignal(cheesyDrive.cheesyDrive(controller.throttle(), controller.wheel(), controller.quickTurn()));
        drive.setGear(controller.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);

        double upDown = (controller.liftUp() ? 1 : 0) - (controller.liftDown() ? 1 : 0);
        superstructure.setHeight(superstructure.getHeight() + upDown * Settings.CLIMBER_FEET_PER_REVOLUTION * (Timer.getFPGATimestamp() - time));
        time = Timer.getFPGATimestamp();
    }

    @Override
    public void testPeriodic() {
    }
}
