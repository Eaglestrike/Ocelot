package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.settings.RobotSettings;

public class GearShifter {
    public enum State {
        HIGH, LOW
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
