package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.util.PercentageRange;

public class DualController implements Controller {

    private final Joystick left;
    private final Joystick right;

    private final double basicDeadband = 0.02;

    private double band(double x) {
        return Math.abs(x) < basicDeadband ? 0 : x;
    }

    public DualController(Joystick left, Joystick right) {
        this.left = left;
        this.right = right;
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

    @Override
    public PercentageRange throttle() {
        return new PercentageRange(adjustThrottle(band(left.getY())));
    }

    @Override
    public PercentageRange wheel() {
        return new PercentageRange(adjustWheel(band(right.getX() * -1)));
    }

    // right trigger
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
        return false;
    }

    @Override
    public boolean liftDown() {
        return false;
    }

    @Override
    public boolean intakeSpinning() {
        return false;
    }

    @Override
    public boolean intakeActuated() {
        return false;
    }

    @Override
    public Carriage.ElevationStage intakeElevationStage() {
        return Carriage.ElevationStage.RAISED;
    }
}
