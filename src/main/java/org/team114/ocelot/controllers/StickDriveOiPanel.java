package org.team114.ocelot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.util.EdgeDetector;
import org.team114.ocelot.settings.Settings;

import java.util.Objects;

public class StickDriveOiPanel implements Controller {

    private final Joystick left;
    private final Joystick right;
    private final Joystick oi;

    private final EdgeDetector.EdgeType toDetect = EdgeDetector.EdgeType.RISING;

    // controller curves here
    private double driveCurve(double x) {
        return x;
    }

    public StickDriveOiPanel(Joystick left, Joystick right, Joystick oi) {
        Objects.requireNonNull(left, "The left cannot be null!");
        Objects.requireNonNull(right, "The right cannot be null!");
        Objects.requireNonNull(oi, "The oi cannot be null!");
        this.left = left;
        this.right = right;
        this.oi = oi;
    }

    // ====== UTIL ======
    private double band(double x) {
        return Math.abs(x) < Settings.Controller.STANDARD_DEADBAND ? 0 : x;
    }

    private double bandBigger(double x) {
        return Math.abs(x) < Settings.Controller.FREER_DEADBAND ? 0 : x;
    }


    // ====== DRIVER ======
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
    public double throttle() {
        return adjustThrottle(band(left.getY()));
    }

    @Override
    public double wheel() {
        return adjustWheel(band(right.getX() * -1));
    }

    // right trigger
    @Override
    public boolean wantQuickTurn() {
        return right.getRawButton(1);
    }

    // left trigger
    @Override
    public boolean wantLowGear() {
        return left.getRawButton(1);
    }

    // ====== OPERATOR ======

    // carriage states
    @Override
    public boolean carriageOpenLowSpeed() {
        return oi.getRawButtonPressed(10);
    }

    @Override
    public boolean carriageOpenIdle() {
        return oi.getRawButtonPressed(12);
    }

    @Override
    public boolean carriageIntake()  {
        return oi.getRawButtonPressed(1);
    }

    @Override
    public boolean carriageClose()  {
        return oi.getRawButtonPressed(4);
    }

    @Override
    public boolean carriageOuttake() {
        return oi.getRawButtonPressed(7);
    }

    @Override
    public boolean cairrageUp() {
        return oi.getRawButtonPressed(2);
    }

    @Override
    public boolean cairrageMiddle() {
        return oi.getRawButtonPressed(5);
    }

    @Override
    public boolean cairrageDown() {
        return oi.getRawButtonPressed(8);
    }

    // lift height
    @Override
    public boolean liftZeroCalibration() {
        return oi.getRawButtonPressed(11);
    }

    @Override
    public double liftHeightSetPoint() {
        return oi.getY();
    }

    @Override
    public boolean wantManualLiftHeight() {
        return oi.getRawButton(3);
    }

    // TODO verify these two button ids
    @Override
    public boolean manualLiftUp() {
//        return oi.getRawButton(15);
        return false;
    }

    @Override
    public boolean manualLiftDown() {
//        return oi.getRawButton(14);
        return false;
    }

    // ====== MISC =====
    @Override
    public boolean speedFaster() {
        return oi.getRawButton(6);
    }

    @Override
    public boolean speedSlower() {
        return oi.getRawButton(9);
    }
}