package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.settings.RobotSettings;

public class GearShifter {
    private final RobotRegistry robotRegistry;
    private final DoubleSolenoid gearSolenoid;
    public GearShifter(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
        RobotSettings.Configuration configuration = this.robotRegistry.getConfiguration();
        this.gearSolenoid = new DoubleSolenoid(
            configuration.getChannelAndRegister("highGearChannel"),
            configuration.getChannelAndRegister("lowGearChannel")
        );
        this.gearSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void setHighGear(boolean on) {
        if (on) {
            gearSolenoid.set(DoubleSolenoid.Value.kForward);
        } else {
            gearSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
    }

    public void shift() {
        switch (gearSolenoid.get()) {
            case kForward:
                gearSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case kReverse:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case kOff:
                gearSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
        }
    }
}
