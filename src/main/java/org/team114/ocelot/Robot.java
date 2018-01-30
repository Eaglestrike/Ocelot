package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.dagger.DaggerRobotComponent;
import org.team114.ocelot.dagger.RobotComponent;
import org.team114.ocelot.modules.control.Controller;
import org.team114.ocelot.subsystems.drive.AbstractDrive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Side;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    Subsystems subsystems = new Subsystems();

    RobotComponent component = DaggerRobotComponent.create();

    SubsystemManager subsystemManager;
    AbstractDrive drive = component.drive();

    Controller controller = component.controller();
    CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper();
    RobotState robotState = component.robotState();

    AutoModeExecutor autoModeExecutor;

    @Override
    public void robotInit() {
        subsystemManager = new SubsystemManager(drive);
        subsystemManager.start();
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
        autoModeExecutor.setAutoMode(new TestMode(subsystems));
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
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        double throttle = controller.throttle();
        double wheel = controller.wheel();
        DriveSignal signal = cheesyDriveHelper.cheesyDrive(throttle, -wheel, controller.quickTurn());
        drive.setSideSpeed(Side.LEFT, -signal.getLeft());
        drive.setSideSpeed(Side.RIGHT, signal.getRight());
    }

    @Override
    public void testPeriodic() {
    }
}
