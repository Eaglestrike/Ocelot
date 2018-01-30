package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.dagger.Primary;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SingleController implements Controller {

    private final Joystick stick;

    @Inject
    public SingleController(@Primary Joystick s) {
        stick = s;
    }

    public double throttle() {
        return stick.getRawAxis(1);
    }

    public double wheel() {
        return stick.getRawAxis(4);
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
