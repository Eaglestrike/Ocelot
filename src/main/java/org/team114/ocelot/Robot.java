package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;

import org.team114.ocelot.auto.FollowPath;
import org.team114.lib.pathgenerator.Path;
import org.team114.ocelot.subsystems.RobotState;
import org.team114.ocelot.util.CheesyDriveHelper;
import org.team114.ocelot.util.DriveSignal;
import org.team114.lib.subsystem.SubsystemManager;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {
    public static final double wheelbase_width = 0.55245;
    public static final double maxVelocity = 1;
    public static final double maxAcceleration = 2;
    public static final double maxCentriAccel = 1;

    private TalonSRX leftMasterTalon;
    private TalonSRX leftSlaveTalon;
    private TalonSRX rightMasterTalon;
    private TalonSRX rightSlaveTalon;
    private Joystick controller;

    private RobotState robotState;

    private FollowPath pathFollower;

    private SubsystemManager subsystemManager;
    
    private final CheesyDriveHelper cheesyDrive = new CheesyDriveHelper();

    @Override
    public void robotInit() {
        controller = new Joystick(0);

        leftMasterTalon = new TalonSRX(1);
        leftSlaveTalon  = new TalonSRX(2);
        rightMasterTalon  = new TalonSRX(3);
        rightSlaveTalon  = new TalonSRX(4);

        for (TalonSRX t :Arrays.asList(leftMasterTalon, rightMasterTalon)) {
            t.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 10, 10);
            t.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 500);
            t.setSelectedSensorPosition(0,0,0);
        }

        leftSlaveTalon.set(ControlMode.Follower, leftMasterTalon.getDeviceID());
        rightSlaveTalon.set(ControlMode.Follower, rightMasterTalon.getDeviceID());

        robotState = new RobotState(leftMasterTalon, rightMasterTalon, wheelbase_width);

        subsystemManager = new SubsystemManager(robotState);
        subsystemManager.start();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopInit() {
        leftMasterTalon.set(ControlMode.PercentOutput, 0);
        rightMasterTalon.set(ControlMode.PercentOutput, 0);
//        robotState.onStart(Timer.getFPGATimestamp());
    }


    @Override
    public void testInit() {
        robotState.resetPosition();

        Path p = new Path(new double[][][] {{{0.0, 3.552713678800501E-15, 0.0, 35.46180555555554, -39.863715277777764, 11.901909722222218}, {0.0, 3.1086244689504383E-15, 0.0, 11.08680555555555, -9.082465277777775, 1.9956597222222219}},
{{7.4999999999999964, 7.440104166666671, -13.277777777777775, -4.807291666666667, 19.68749999999999, -9.042534722222214}, {4.000000000000001, 7.908854166666665, -0.7777777777777799, -5.119791666666664, 0.9375000000000008, 1.0512152777777768}}});

        pathFollower = new FollowPath(p, Timer.getFPGATimestamp());
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
        double throttle = -controller.getRawAxis(1);
        double turn = controller.getRawAxis(4);
        boolean quickturn = controller.getRawButton(5);
        DriveSignal d = cheesyDrive.cheesyDrive(throttle, turn, quickturn);
        leftMasterTalon.set(ControlMode.PercentOutput, d.getLeft());
        rightMasterTalon.set(ControlMode.PercentOutput, -d.getRight());
        SmartDashboard.putNumber("L Command", d.getLeft());
        SmartDashboard.putNumber("R Command", d.getRight());

//        robotState.onStep(Timer.getFPGATimestamp());
    }


    @Override
    public void testPeriodic() {
        double[] out = pathFollower.tick(Timer.getFPGATimestamp(), robotState.currentPose);
        leftMasterTalon.set(ControlMode.Velocity, out[0]);
        rightMasterTalon.set(ControlMode.Velocity, out[1]);
        SmartDashboard.putNumber("Path Follow Output Left", out[0]);
        SmartDashboard.putNumber("Path Follow Output Right", out[1]);
    }
}
