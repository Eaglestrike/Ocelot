package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.lib.util.EdgeDetector;
import org.team114.ocelot.util.PercentageRange;

public class SingleController implements Controller {

    private final Joystick stick;

    public SingleController(Joystick s) {
        stick = s;
    }

    @Override
    public PercentageRange throttle() {
        return new PercentageRange(stick.getRawAxis(1));
    }

    @Override
    public PercentageRange wheel() {
        return new PercentageRange(stick.getRawAxis(4));
    }

    @Override
    public boolean startLift() {
        return stick.getRawButton(1);
    }

    @Override
    public boolean endLift() {
        return stick.getRawButton(2);
    }

    @Override
    public boolean intake() {
        return stick.getRawButton(3);
    }

    @Override
    public boolean quickTurn() {
        return stick.getRawButton(4);
    }

    @Override
    public boolean startClimb() {
        return stick.getRawButton(5);
    }

    @Override
    public boolean endClimb() {
        return stick.getRawButton(6);
    }

    private boolean shiftGearButton() {
        return stick.getRawButton(7);
    }

    EdgeDetector shiftEdge = new EdgeDetector(this::shiftGearButton);
    @Override
    public EdgeDetector shiftGear() {
        return shiftEdge;
    }
}
