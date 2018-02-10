package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
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

    @Override
    public PercentageRange throttle() {
        return new PercentageRange(band(left.getY()));
    }

    @Override
    public PercentageRange wheel() {
        return new PercentageRange(band(right.getX() * -1));
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
    public boolean intakeSpinning() {
        return false;
    }

    @Override
    public boolean intakeActuated() {
        return false;
    }

    @Override
    public boolean liftUp() {
        return false;
    }

    @Override
    public boolean liftDown() {
        return false;
    }
}
