package org.team114.ocelot.util;

import org.team114.ocelot.Registry;
import org.team114.ocelot.settings.Settings;

public class CheesyDriveHelper {
    private static final double WHEEL_CONTROLLER_REVERSED = -1.0;
    private final Registry registry;

    private double quickStopAccumulator;
    private final double throttleDeadband;
    private final double wheelDeadband;
    private final double turnSensitivity;

    public CheesyDriveHelper(Registry registry) {
        this.registry = registry;

        Settings.Configuration configuration = this.registry.getConfiguration();
        this.throttleDeadband = configuration.getDouble("throttleDeadband");
        this.wheelDeadband = configuration.getDouble("wheelDeadband");
        this.turnSensitivity = configuration.getDouble("turnSensitivity");
    }

    public DriveSignal cheesyDrive(PercentageRange throttlePercentage, PercentageRange wheelPercentage, boolean isQuickTurn) {

        double wheel = wheelPercentage.deadband(this.wheelDeadband).scaled(WHEEL_CONTROLLER_REVERSED);
        double throttle = throttlePercentage.deadband(throttleDeadband).unscaled();

        double overPower, angularPower;

        if (isQuickTurn) {
            if (Math.abs(throttle) < 0.2) {
                double alpha = 0.1;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator + alpha * wheel * 2;
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
}
