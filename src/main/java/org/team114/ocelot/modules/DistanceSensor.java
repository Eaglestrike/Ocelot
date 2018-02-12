package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import org.team114.ocelot.settings.Settings;

public class DistanceSensor {

    private final AnalogInput distanceSensor;

    private final double maxDistance = Settings.DistanceSensor.MAX_DISTANCE;
    private final double minDistance = Settings.DistanceSensor.MIN_DISTANCE;
    private final double maxVoltage = Settings.DistanceSensor.MAX_VOLTAGE;
    private final double minVoltage = Settings.DistanceSensor.MIN_VOLTAGE;

    public DistanceSensor(AnalogInput distanceSensor) {
        this.distanceSensor = distanceSensor;
    }

    /**
     * 3.1V at 4cm to 0.3V at 30cm
     * @return distance the sensor reads, in feet
     */
    public double get() {
        double voltage = distanceSensor.getVoltage();

        // assuming a linear relationship
        // d(v) = [(30-4)/(0.3-3.1)](v-0.3) + 30
        double slope = (maxDistance - minDistance) / (maxVoltage - minVoltage);
        double distance = slope * (voltage - maxVoltage) + maxDistance;

        return Settings.FEET_PER_CENTIMETER * distance;
    }
}
