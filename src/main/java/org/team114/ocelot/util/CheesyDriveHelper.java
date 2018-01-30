package org.team114.ocelot.util;

import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.settings.RobotSettings;

public class CheesyDriveHelper {
    private final RobotRegistry robotRegistry;

    public static double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }

    double quickStopAccumulator;
    private final double throttleDeadband;
    private final double wheelDeadband;
    private final double turnSensitivity;

    public CheesyDriveHelper(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;

        RobotSettings.Configuration configuration = this.robotRegistry.getConfiguration();
        this.throttleDeadband = configuration.getDouble("throttleDeadband");
        this.wheelDeadband = configuration.getDouble("wheelDeadband");
        this.turnSensitivity = configuration.getDouble("turnSensitivity");
    }
    public DriveSignal cheesyDrive(double throttle, double wheel, boolean isQuickTurn) {

        wheel = handleDeadband(wheel, wheelDeadband);
        throttle = handleDeadband(throttle, throttleDeadband);

        double overPower, angularPower;

        if (isQuickTurn) {
            if (Math.abs(throttle) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * limit(wheel, 1.0) * 2;
            }
            overPower = 1.0;
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * turnSensitivity - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        double rightPwm = throttle - angularPower;
        double leftPwm = throttle + angularPower;
        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }

        return new DriveSignal(leftPwm, rightPwm, false);
    }

    private double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
}
