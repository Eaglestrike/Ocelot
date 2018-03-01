package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.util.PercentageRange;

public class DualController implements Controller {

    private final Joystick left;
    private final Joystick right;
    private final Joystick operator;

    private final double basicDeadband = 0.02;
    private Carriage.ElevationStage lastState;

    public DualController(Joystick left, Joystick right, Joystick operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        lastState = Carriage.ElevationStage.RAISED;
    }

    private static double adjustThrottle(double throttle) {
        final double denominator = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.THROTTLE_GROWTH);
        // Apply a sin function that's scaled to make it feel better.
        throttle = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.THROTTLE_GROWTH * throttle) / denominator;
        throttle = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.THROTTLE_GROWTH * throttle) / denominator;
        return throttle;
    }

    private static double adjustWheel(double wheel) {
        final double denominator = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.WHEEL_GROWTH);
        // Apply a sin function that's scaled to make it feel better.
        wheel = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.WHEEL_GROWTH * wheel) / denominator;
        wheel = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.WHEEL_GROWTH * wheel) / denominator;
        wheel = Math.sin(Math.PI / 2.0 * Settings.CheesyDriveHelper.WHEEL_GROWTH * wheel) / denominator;
        return wheel;
    }

    private double band(double x) {
        return Math.abs(x) < basicDeadband ? 0 : x;
    }

    @Override
    public PercentageRange throttle() {
        return new PercentageRange(adjustThrottle(band(left.getY())));
    }

    @Override
    public PercentageRange wheel() {
        return new PercentageRange(adjustWheel(band(right.getX() * -1)));
    }

    // right trigger
    @Override
    public boolean quickTurn() {
        return right.getRawButton(1);
    }

    // left trigger
    @Override
    public boolean wantLowGear() {
        return left.getRawButton(1);
    }

    @Override
    public boolean liftUp() {
        return band(operator.getY()) > 0;
    }

    @Override
    public boolean liftDown() {
        return band(operator.getY()) < 0;
    }

    @Override
    public boolean spinIntakeIn() {
        return operator.getRawButton(2);
    }

    @Override
    public boolean spinIntakeOut() {
        return operator.getRawButton(6);
    }

    @Override
    public boolean intakeActuated() {
        return operator.getRawButton(1);
    }

    @Override
    public Carriage.ElevationStage intakeElevationStage() {
        if (operator.getRawButton(3)) {
            return lastState = Carriage.ElevationStage.RAISED;
        } else if (operator.getRawButton(4)) {
            return lastState = Carriage.ElevationStage.MIDDLE;
        } else if (operator.getRawButton(5)) {
            return lastState = Carriage.ElevationStage.LOWERED;
        }
        return lastState;
    }
}
