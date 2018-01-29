package org.team114.ocelot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.subsystem.SubsystemManager;
import org.team114.ocelot.auto.AutoModeExecutor;
import org.team114.ocelot.auto.modes.TestMode;
import org.team114.ocelot.modules.DualController;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Side;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    SubsystemManager subsystemManager;
    AbstractDrive drive;

    DualController controller;
    CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper();
    Gyro gyro = Gyro.shared;
    RobotState robotState = RobotState.shared;

    AutoModeExecutor autoModeExecutor;

    @Override
    public void robotInit() {
        RobotSide leftSide = new RobotSide(
            new TalonSRX(1),
            new TalonSRX(2)
        );
        RobotSide rightSide = new RobotSide(
            new TalonSRX(3),
            new TalonSRX(4)
        );

        controller = new DualController(new Joystick(0), new Joystick(1));
        drive = new Drive(leftSide, rightSide, gyro, robotState);

        subsystemManager = new SubsystemManager(drive);
        SubsystemSingletons.drive = drive;
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
        autoModeExecutor.setAutoMode(new TestMode());
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
