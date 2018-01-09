package org.team114.robot2018;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.team114.lib.subsystem.SubsystemManager;

/**
 * Main robot2018 class, which acts as the root for ownership and control of the robot2018.
 */
public class Robot extends IterativeRobot {

    SubsystemManager subsystemManager;

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
