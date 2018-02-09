package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.settings.Settings;

public class GearShifter {
    public enum State {
        HIGH, LOW
    }

    private final DoubleSolenoid gearSolenoid = new DoubleSolenoid(
            Settings.GearShifter.HIGH_GEAR_CHANNEL,
            Settings.GearShifter.LOW_GEAR_CHANNEL);


    public void set(State state) {
        switch (state) {
            case LOW:
                gearSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            default: // high
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
        }
    }

    public State get() {
        switch (gearSolenoid.get()) {
            case kReverse:
                return State.LOW;
            default: // high
                return State.HIGH;
        }
    }
}
