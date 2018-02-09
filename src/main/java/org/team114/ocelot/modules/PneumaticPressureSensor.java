package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import org.team114.ocelot.settings.Settings;

public class PneumaticPressureSensor {

    private AnalogInput pressureSensor;

    public PneumaticPressureSensor(AnalogInput pressureSensor) {
        this.pressureSensor = pressureSensor;
    }

    // Link to documentation: http://www.revrobotics.com/content/docs/REV-11-1107-DS.pdf
    public double getPressure() {
        double Vcc = Settings.TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE;
        return (250.0 * (pressureSensor.getVoltage() / Vcc)) - 25.0;
    }
}
