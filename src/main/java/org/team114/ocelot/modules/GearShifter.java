package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.Registry;
import org.team114.ocelot.settings.Settings;

public class GearShifter {
    public enum State {
        HIGH, LOW, OFF
    }

    private final Registry registry;
    private final DoubleSolenoid gearSolenoid;
    public GearShifter(Registry registry) {
        this.registry = registry;
        Settings.Configuration configuration = this.registry.getConfiguration();
        this.gearSolenoid = new DoubleSolenoid(
            configuration.getChannelAndRegister("highGearChannel"),
            configuration.getChannelAndRegister("lowGearChannel")
        );
        set(State.HIGH);
    }

    public void set(State state) {
        switch (state) {
            case HIGH:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case LOW:
                gearSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case OFF:
                gearSolenoid.set(DoubleSolenoid.Value.kOff);
                break;
        }
    }

    public State get() {
        switch (gearSolenoid.get()) {
            case kForward:
                return State.HIGH;
            case kReverse:
                return State.LOW;
            default: // kOff
                return State.OFF;
        }
    }

    public void shift() {
        switch (get()) {
            case LOW:
                set(State.HIGH);
                break;
            case HIGH:
                set(State.LOW);
                break;
            case OFF:
                set(State.HIGH);
                break;
        }
    }
}
