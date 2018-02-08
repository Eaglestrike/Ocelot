package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.settings.Configuration;

public class GearShifter {
    public enum State {
        HIGH, LOW
    }

    private final DoubleSolenoid gearSolenoid;
    public GearShifter(Configuration configuration) {
        this.gearSolenoid = new DoubleSolenoid(
            configuration.getChannelAndRegister("highGearChannel"),
            configuration.getChannelAndRegister("lowGearChannel")
        );
        set(State.HIGH);
    }

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
