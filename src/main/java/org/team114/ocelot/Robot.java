package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;

import org.team114.ocelot.auto.FollowPath;
import org.team114.ocelot.pathgenerator.Path;
import org.team114.ocelot.subsystems.RobotState;
import org.team114.ocelot.subsystems.SubsystemManager;

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
    public static final double maxAccel = 2;
    public static final double maxCentriAccel = 1;

    private TalonSRX leftMasterTalon;
    private TalonSRX leftSlaveTalon;
    private TalonSRX rightMasterTalon;
    private TalonSRX rightSlaveTalon;
    private Joystick controller;

    private RobotState robotState;

    private FollowPath pathFollower;

    private SubsystemManager subsystemManager;

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

        RobotState robotState = new RobotState(leftMasterTalon, rightMasterTalon, wheelbase_width);

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
    }


    @Override
    public void testInit() {
        robotState.resetPosition();

        Path p = new Path(new double[][][]{{{0.0, 1.9999999999999978, 0.0, 12.670138888888895, -13.436631944444448, 3.766493055555556}, {0.0, -1.1102230246251565E-16, 0.0, 3.5868055555555554, -3.4574652777777772, 0.870659722222222}},
{{4.999999999999998, 6.0963541666666705, -4.444444444444445, -3.2447916666666687, 5.437499999999999, -1.8446180555555542}, {0.9999999999999999, 2.283854166666667, -0.7777777777777781, -1.3697916666666663, 0.9374999999999998, -0.07378472222222217}}});

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
        double throttle = -controller.getRawAxis(1); //leftY
        double turn = controller.getRawAxis(4); //right X
        leftMasterTalon.set(ControlMode.PercentOutput, (throttle + turn) / 2);
        rightMasterTalon.set(ControlMode.PercentOutput, (throttle - turn) / 2);
        SmartDashboard.putNumber("L Command", (throttle + turn) / 2);
        SmartDashboard.putNumber("R Command", (throttle - turn) / 2);
    }


    @Override
    public void testPeriodic() {
        double[] out = pathFollower.tick(Timer.getFPGATimestamp(), robotState.currentPose);
        rightMasterTalon.set(ControlMode.Velocity, out[1]);
        leftMasterTalon.set(ControlMode.Velocity, out[0]);
    }
}
