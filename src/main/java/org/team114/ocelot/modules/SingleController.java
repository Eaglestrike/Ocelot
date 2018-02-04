package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.util.PercentageRange;

public class SingleController implements Controller {

    private final Joystick stick;


    public SingleController(Joystick s) {
        stick = s;
    }

    public PercentageRange throttle() {
        return new PercentageRange(stick.getRawAxis(1));
    }

    public PercentageRange wheel() {
        return new PercentageRange(stick.getRawAxis(4));
    }

    public boolean startLift() {
        return stick.getRawButton(1);
    }

    public boolean endLift() {
        return stick.getRawButton(2);
    }

    public boolean intake() {
        return stick.getRawButton(3);
    }

    public boolean quickTurn() {
        return stick.getRawButton(4);
    }

    public boolean startClimb() {
        return stick.getRawButton(5);
    }

    public boolean endClimb() {
        return stick.getRawButton(3);
    }

}
