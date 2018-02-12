package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.settings.Settings;

public class GearShifter {
    public enum State {
        HIGH, LOW
    }

    private final DoubleSolenoid gearSolenoid = new DoubleSolenoid(
            Settings.GearShifter.HIGH_GEAR,
            Settings.GearShifter.LOW_GEAR);


    public void set(State state) {
        switch (state) {
            case LOW:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case HIGH:
                gearSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }

    public State get() {
        switch (gearSolenoid.get()) {
            case kForward:
                return State.LOW;
            case kReverse:
                return State.HIGH;
            default: // maybe throw exception?
                return State.HIGH;
        }
    }
}
