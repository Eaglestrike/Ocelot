package org.team114.ocelot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.util.PercentageRange;

public class DualController implements Controller {

    private final Joystick left;
    private final Joystick right;
    private final Joystick operator;

    private final double basicDeadband = 0.02;
    private Carriage.ElevationStage lastState;

    // ====== UTIL ======
    private double band(double x) {
        return Math.abs(x) < basicDeadband ? 0 : x;
    }

    private double bandBigger(double x) {
        return Math.abs(x) < 0.15 ? 0 : x;
    }


    public DualController() {
        this.left = new Joystick(0);
        this.right = new Joystick(1);
        this.operator = new Joystick(2);
        lastState = Carriage.ElevationStage.RAISED;
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

    // ====== OPERATOR ======

    // carriage states
    @Override
    public boolean carriageOpen() {
        return operator.getRawButton(2);
    }

    @Override
    public boolean carriageIntake()  {
        return operator.getRawButton(1);
    }

    @Override
    public boolean carriageClose()  {
        return operator.getRawButton(4);
    }

    @Override
    public boolean carriageOuttake() {
        return operator.getRawButton(3);
    }

    @Override
    public Carriage.ElevationStage intakeElevation() {
        if (operator.getRawButton(5)) {
            return lastState = Carriage.ElevationStage.RAISED;
        } else if (operator.getRawAxis(2) > 0.75) {
            return lastState = Carriage.ElevationStage.MIDDLE;
        } else if (operator.getRawAxis(3) > 0.75) {
            return lastState = Carriage.ElevationStage.LOWERED;
        }
        return lastState;
    }

    // lift height
    @Override
    public double liftIncrement() {
//        return (operator.getRawButton(1) ? 1.0 : 0) + (operator.getRawButton(1) ? -1.0 : 0);
        return bandBigger(-operator.getRawAxis(5));
    }

    @Override
    public boolean lowHeight() {
        System.out.println("pov: " + operator.getPOV(0));
        return operator.getPOV(0) == 180;
    }

    @Override
    public boolean switchHeight() {
        return operator.getPOV(0) == 270;
    }

    @Override
    public boolean scaleHeight() {
        return operator.getPOV(0) == 0;
    }

    @Override
    public boolean liftZeroCalibration() {
        return operator.getRawButton(10);
    }
}
