package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.settings.RobotSettings;

public class GearShifter {
    private final RobotRegistry robotRegistry;
    private final Solenoid highGearSolenoid;
    private final Solenoid lowGearSolenoid;
    public GearShifter(RobotRegistry robotRegistry) {
        this.robotRegistry = robotRegistry;
        RobotSettings.Configuration configuration = this.robotRegistry.getConfiguration();
        this.highGearSolenoid = new Solenoid(configuration.getChannelAndRegister("highGearChannel"));
        this.lowGearSolenoid = new Solenoid(configuration.getChannelAndRegister("lowGearChannel"));
    }

    public void setHighGear(boolean on) {
        // always disable the opposite solenoid first to avoid possible "fighting"
        if (on) {
            this.lowGearSolenoid.set(false);
            this.highGearSolenoid.set(true);
        } else {
            this.highGearSolenoid.set(false);
            this.lowGearSolenoid.set(false);
        }
    }
}
