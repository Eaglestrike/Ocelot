package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.util.PercentageRange;

public class SingleController implements Controller {

    private final Joystick stick;

    public SingleController(Joystick stick) {
        this.stick = stick;
    }

    @Override
    public PercentageRange throttle() {
        return new PercentageRange(-1 * stick.getRawAxis(1));
    }

    @Override
    public PercentageRange wheel() {
        return new PercentageRange(stick.getRawAxis(4));
    }

    @Override
    public boolean quickTurn() {
        return stick.getRawButton(4);
    }

    @Override
    public boolean wantLowGear() {
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

    @Override
    public boolean spinIntakeIn() {
        return false;
    }

    @Override
    public boolean spinIntakeOut() {
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
