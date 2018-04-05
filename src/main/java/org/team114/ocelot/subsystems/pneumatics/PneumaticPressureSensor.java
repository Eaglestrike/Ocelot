package org.team114.ocelot.subsystems.pneumatics;

import edu.wpi.first.wpilibj.AnalogInput;
import org.team114.ocelot.settings.Settings;

import javax.inject.Singleton;

@Singleton
class PneumaticPressureSensor {

    private AnalogInput pressureSensor;

    PneumaticPressureSensor(AnalogInput pressureSensor) {
        this.pressureSensor = pressureSensor;
    }

    // Link to documentation: http://www.revrobotics.com/content/docs/REV-11-1107-DS.pdf
    double getPressure() {
        double Vcc = Settings.TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE;
        return (250.0 * (pressureSensor.getVoltage() / Vcc)) - 25.0;
    }
}
