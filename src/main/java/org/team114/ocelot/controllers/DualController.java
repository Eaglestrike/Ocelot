package org.team114.ocelot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.modules.Carriage;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.util.PercentageRange;

public class DualController implements Controller {

    private final Joystick left;
    private final Joystick right;
    private final XboxController xbox;

    private Carriage.ElevationStage lastState;

    public DualController(Joystick left, Joystick right, XboxController xbox) {
        this.left = left;
        this.right = right;
        this.xbox = xbox;

        lastState = Carriage.ElevationStage.RAISED;
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
        return xbox.b();
    }

    @Override
    public boolean carriageIntake()  {
        return xbox.a();
    }

    @Override
    public boolean carriageClose()  {
        return xbox.y();
    }

    @Override
    public boolean carriageOuttake() {
        return xbox.x();
    }

    @Override
    public Carriage.ElevationStage intakeElevation() {
        if (xbox.leftBumper()) {
            return lastState = Carriage.ElevationStage.RAISED;
        } else if (xbox.leftTrigger() > 0.75) {
            return lastState = Carriage.ElevationStage.MIDDLE;
        } else if (xbox.rightTrigger() > 0.75) {
            return lastState = Carriage.ElevationStage.LOWERED;
        }
        return lastState;
    }

    // lift height
    @Override
    public double liftIncrement() {
        return bandBigger(-xbox.rightYAxis());
    }

    @Override
    public boolean lowHeight() {
        System.out.println("pov: " + xbox.arrowPad());
        return xbox.arrowPad() == 180;
    }

    @Override
    public boolean switchHeight() {
        return xbox.arrowPad() == 270;
    }

    @Override
    public boolean scaleHeight() {
        return xbox.arrowPad() == 0;
    }

    @Override
    public boolean liftZeroCalibration() {
        return xbox.rightStickPressed();
    }
}
