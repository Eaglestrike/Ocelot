package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.AutoModeSelector;
import org.team114.ocelot.factory.ControllerFactory;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.*;
import org.team114.ocelot.subsystems.drive.*;
import org.team114.ocelot.subsystems.pneumatics.PneumaticPressureSensor;
import org.team114.ocelot.subsystems.pneumatics.StandardPneumatics;
import org.team114.ocelot.subsystems.superstructure.Carriage;
import org.team114.ocelot.subsystems.superstructure.GP2YProximitySensor;
import org.team114.ocelot.subsystems.superstructure.Lift;
import org.team114.ocelot.subsystems.superstructure.StandardSuperstructure;
import org.team114.ocelot.util.CheesyDriveHelper;

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
    private AutoModeSelector autoModeSelector;
    private RobotState robotState = new RobotState();

    // subsystems
    private Drive drive;
    private Superstructure superstructure;
    private Pneumatics pneumatics;

    // modules
    private Gyro gyro;
    private GearShifter gearShifter;
    private DriveSide leftSide;
    private DriveSide rightSide;
    private Carriage carriage;
    private Lift lift;

    private PneumaticPressureSensor pressureSensor;
    private TeleopInputDelegator teleopInputDelegator;

    // testing
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

        // create modules
        gyro = NavXGyro.shared;
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
                new GP2YProximitySensor(new AnalogInput(Settings.DistanceSensor.CHANNEL)));
        liftMaster = new775ProTalon(Settings.Lift.MASTER);
        liftSlave = new775ProTalon(Settings.Lift.SLAVE);
        lift = new Lift(
                liftMaster,
                liftSlave);

        // create subsystems
        drive = new StandardDrive(
                robotState,
                gyro,
                leftSide,
                rightSide,
                gearShifter);
        superstructure = new StandardSuperstructure(
                carriage,
                lift);
        pneumatics = new StandardPneumatics(
                new Compressor(),
                pressureSensor);

        pneumatics.setMinimumPressure(45);
        pneumatics.setMinimumPressure(85);

        // create general stuff
        autoModeExecutor = new AutoModeExecutor();
        autoModeSelector = new AutoModeSelector(drive, superstructure, robotState);
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

        autoModeExecutor.setAutoMode(autoModeSelector.getAutoMode());
        autoModeExecutor.start();
    }

    @Override
    public void teleopInit() {
        autoModeExecutor.stop();
        //TODO VENTURA add more reset stuff
        drive.prepareForTeleop();
        this.teleopInputDelegator = new TeleopInputDelegator(drive, superstructure,
                ControllerFactory.stickDriveOiPanel(), new CheesyDriveHelper());
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

    @Override
    public void teleopPeriodic() {
        this.teleopInputDelegator.defaultStep();
    }

    @Override
    public void testInit() {
        pneumatics.setMinimumPressure(-1);
    }

    @Override
    public void testPeriodic() {
    }
}

