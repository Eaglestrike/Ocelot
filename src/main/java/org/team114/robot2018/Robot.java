package org.team114.robot2018;

import edu.wpi.first.wpilibj.IterativeRobot;

import java.util.Arrays;

import org.team114.robot2018.subsystems.SubsystemManager;
import org.team114.robot2018.subsystems.RobotState;
import org.team114.robot2018.subsystems.Subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {

    SubsystemManager subsystemManager;
    
    /*
     * TODO: Somehow make the talons work
     * Preferably by linking the encoders to the motors.
     */
    
    private TalonSRX rightEncoder = new TalonSRX(0);
    private TalonSRX leftEncoder = new TalonSRX(1);
    
    private TalonSRX rightMotor = new TalonSRX(2);
    private TalonSRX leftMotor = new TalonSRX(3);

    @Override
    public void robotInit() {
        subsystemManager = new SubsystemManager();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
       
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
    }

    @Override
    public void testPeriodic() {
    }
}
