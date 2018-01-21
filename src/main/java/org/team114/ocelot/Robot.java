package org.team114.ocelot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.team114.lib.subsystem.SubsystemManager;

/**
 * Main ocelot class, which acts as the root for ownership and control of the ocelot.
 */
public class Robot extends IterativeRobot {
    public static final double wheelbase_width = 0.55245;
    public static final double maxVelocity = 1;
    public static final double maxAcceleration = 2;
    public static final double maxCentriAccel = 1;

    SubsystemManager subsystemManager = new SubsystemManager();

    @Override
    public void robotInit() {
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
