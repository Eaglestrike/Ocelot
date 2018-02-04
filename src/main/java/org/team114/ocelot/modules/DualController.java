package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.util.PercentageRange;

public class DualController implements Controller {

    private final Joystick left;
    private final Joystick right;

    public DualController(Joystick left, Joystick right) {
        this.left = left;
        this.right = right;
    }

    public PercentageRange throttle() {
        return new PercentageRange(left.getY());
    }

    public PercentageRange wheel() {
        return new PercentageRange(right.getX());
    }

    public boolean startLift() {
        return left.getRawButton(1);
    }

    public boolean endLift() {
        return left.getRawButton(2);
    }

    public boolean intake() {
        return left.getRawButton(3);
    }

    public boolean quickTurn() {
        return right.getRawButton(1);
    }

    public boolean startClimb() {
        return right.getRawButton(2);
    }

    public boolean endClimb() {
        return right.getRawButton(3);
    }

}

