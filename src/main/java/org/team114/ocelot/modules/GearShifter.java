package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.settings.RobotSettings;

public class GearShifter {
    public enum State {
        HIGH, LOW, OFF
    }

    private final RobotRegistry robotRegistry;
    private final DoubleSolenoid gearSolenoid;
    public GearShifter(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
        RobotSettings.Configuration configuration = this.robotRegistry.getConfiguration();
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
