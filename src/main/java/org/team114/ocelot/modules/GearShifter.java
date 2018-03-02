package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class GearShifter {
    public enum State {
        HIGH, LOW
    }

    private final DoubleSolenoid gearSolenoid;

    public GearShifter(DoubleSolenoid gearSolenoid) {
        this.gearSolenoid = gearSolenoid;
    }

    public void set(State state) {
        switch (state) {
            case HIGH:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case LOW:
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
