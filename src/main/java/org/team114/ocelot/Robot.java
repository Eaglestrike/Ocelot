package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.ControlMode;
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

import static org.team114.ocelot.factory.TalonFactory.new775ProTalon;
import static org.team114.ocelot.factory.TalonFactory.newCimTalon;

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
    private RobotState robotState = new RobotState();

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
    //Temporary fix
    private double time = 0;


    //testing
    TalonSRX liftMaster;
    TalonSRX liftSlave;
    Joystick testing;

    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {

        // create driver-facing stuff
        pressureSensor = new PneumaticPressureSensor(new AnalogInput(Settings.Pneumatics.PNEUMATIC_PRESSURE_SENSOR_ID));
        cheesyDrive = new CheesyDriveHelper();
        controller = new DualController(new Joystick(0), new Joystick(1), new Joystick(2));

        // create modules
        gyro = Gyro.shared;
        gearShifter = new GearShifter(
                new DoubleSolenoid(
                        Settings.GearShifter.HIGH_GEAR,
                        Settings.GearShifter.LOW_GEAR));
        leftSide = new DriveSide(
                newCimTalon(Settings.DriveSide.LEFT_MASTER),
                newCimTalon(Settings.DriveSide.LEFT_SLAVE));
        rightSide = new DriveSide(
                newCimTalon(Settings.DriveSide.RIGHT_MASTER),
                newCimTalon(Settings.DriveSide.RIGHT_SLAVE));
        carriage = new Carriage(
                new Solenoid(Settings.Carriage.INTAKE_CHANNEL),
                new Solenoid(Settings.Carriage.LIFT_STAGE_ONE),
                new Solenoid(Settings.Carriage.LIFT_STAGE_TWO),
                new775ProTalon(Settings.Carriage.LEFT_SPINNER),
                new775ProTalon(Settings.Carriage.RIGHT_SPINNER),
                new DistanceSensor(new AnalogInput(Settings.DistanceSensor.CHANNEL)));
        liftMaster = new775ProTalon(Settings.Lift.MASTER);
        liftSlave = new775ProTalon(Settings.Lift.SLAVE);
        lift = new Lift(
                liftMaster,
                liftSlave,
                new DigitalInput(Settings.Lift.MID_LIMIT_SWITCH));

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
//        pneumatics = new Pneumatics(
//                new Compressor(),
//                pressureSensor);
//        pneumatics.setMinimumPressure(0);
//        pneumatics.setPressureMargin(-1);

        // create general stuff
        autoModeExecutor = new AutoModeExecutor();
        subsystemManager = new SubsystemManager(
                drive,
                superstructure//,
//                pneumatics
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
    public void robotPeriodic() {
//        pneumaticPressureDB.put(pneumatics.getPressure());
        pneumaticPressureDB.put(pressureSensor.getPressure());

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

    @Override
    public void teleopPeriodic() {
        drive.setDriveSignal(cheesyDrive.cheesyDrive(controller.throttle(), controller.wheel(), controller.quickTurn()));
        drive.setGear(controller.wantLowGear() ? GearShifter.State.LOW : GearShifter.State.HIGH);

        superstructure.actuateCarriage(controller.intakeActuated());
        superstructure.actuateCarriageLift(controller.intakeElevationStage());

        if (controller.spinIntakeIn() && !controller.spinIntakeOut()) {
            superstructure.spinCarriage(Settings.Carriage.INTAKE_IN_COMMAND);
        } else if (!controller.spinIntakeIn() && controller.spinIntakeOut()) {
            superstructure.spinCarriage(Settings.Carriage.INTAKE_OUT_COMMAND);
        } else {
            superstructure.spinCarriage(0);
        }

        double upDown = (controller.liftUp() ? 1 : 0) - (controller.liftDown() ? 1 : 0);
        superstructure.setHeight(superstructure.getHeight() + upDown *
                Settings.Lift.NORMAL_SPEED * (Timer.getFPGATimestamp() - time));
        time = Timer.getFPGATimestamp();
    }

    @Override
    public void testInit() {
        testing = new Joystick(2);
        subsystemManager.stop();
    }

    @Override
    public void testPeriodic() {
        liftMaster.set(ControlMode.PercentOutput, testing.getRawAxis(1)*0.4);
        System.out.println(testing.getRawAxis(1));
        System.out.println(liftMaster.getSensorCollection().isFwdLimitSwitchClosed());
        System.out.println(liftMaster.getSensorCollection().isRevLimitSwitchClosed());
    }
}
