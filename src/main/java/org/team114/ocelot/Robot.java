package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.lib.util.DashboardHandle;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.AutoModeSelector;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Pneumatics;
import org.team114.ocelot.subsystems.Superstructure;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    // dagger
    private RobotComponent robotComponent;

    // handles
    private final DashboardHandle countdownDB = new DashboardHandle("Climbing Countdown");
    private final DashboardHandle pneumaticPressureDB = new DashboardHandle("Pneumatic Pressure");
    private final DashboardHandle gearDB = new DashboardHandle("Gear");

    // general
    private SubsystemManager subsystemManager;
    private AutoModeExecutor autoModeExecutor;
    private AutoModeSelector autoModeSelector;

    // subsystems
    private Drive drive;
    private Superstructure superstructure;
    private Pneumatics pneumatics;


    private TeleopInputDelegator teleopInputDelegator;


    /**
     * The main purpose of robot init is to create the mappings between physical objects and their representations.
     * That means, all talons, solenoids, etc. are created here.
     */
    @Override
    public void robotInit() {

        robotComponent = DaggerRobotComponent.create();

        // create subsystems
        drive = robotComponent.drive();
        superstructure = robotComponent.superstructure();
        pneumatics = robotComponent.pneumatics();

        pneumatics.setMinimumPressure(50);
        pneumatics.setPressureMargin(40);

        // create general stuff
        autoModeExecutor = new AutoModeExecutor();
        autoModeSelector = robotComponent.autoModeSelector();
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
        drive.zeroAllSensors();
        drive.prepareForAuto();

        autoModeExecutor.setAutoMode(autoModeSelector.getAutoMode());
        autoModeExecutor.start();
    }

    @Override
    public void teleopInit() {
        autoModeExecutor.stop();
        //TODO VENTURA add more reset stuff
        drive.prepareForTeleop();
        this.teleopInputDelegator = robotComponent.teleopInputDelegator();
    }

    @Override
    public void robotPeriodic() {
        pneumaticPressureDB.put(pneumatics.getPressure());

        // calculates how much time the driver has until they should start climbing, and sends to dashboard
        double timeLeft = Math.round(Settings.GAME_TIME - Timer.getMatchTime() - Settings.CLIMBING_TIME_ESTIMATE);
        countdownDB.put(timeLeft);

        switch (drive.getGear()) {
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
        superstructure.setWantOpenIdle();
    }

    @Override
    public void testPeriodic() {
    }
}

