package org.team114.ocelot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import org.team114.lib.util.InterpolatingTreeMap;
import org.team114.lib.util.InterpolatingDouble;

public class ProximitySensorGP2Y0A41SK0F {

    private final AnalogInput distanceSensor;
    private final InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> voltageToDistanceCm;

    public ProximitySensorGP2Y0A41SK0F(AnalogInput distanceSensor) {
        this.distanceSensor = distanceSensor;
        voltageToDistanceCm = new InterpolatingTreeMap<>();

        //TODO test
        // https://www.pololu.com/file/0J713/GP2Y0A41SK0F.pdf
        voltageToDistanceCm.put(new InterpolatingDouble(0.32), new InterpolatingDouble(40.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.38), new InterpolatingDouble(35.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.43), new InterpolatingDouble(30.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.53), new InterpolatingDouble(25.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.67), new InterpolatingDouble(20.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.76), new InterpolatingDouble(18.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.81), new InterpolatingDouble(16.0));
        voltageToDistanceCm.put(new InterpolatingDouble(0.94), new InterpolatingDouble(14.0));
        voltageToDistanceCm.put(new InterpolatingDouble(1.07), new InterpolatingDouble(12.0));
        voltageToDistanceCm.put(new InterpolatingDouble(1.27), new InterpolatingDouble(10.0));
        voltageToDistanceCm.put(new InterpolatingDouble(1.40), new InterpolatingDouble(9.0));
        voltageToDistanceCm.put(new InterpolatingDouble(1.57), new InterpolatingDouble(8.0));
        voltageToDistanceCm.put(new InterpolatingDouble(1.78), new InterpolatingDouble(7.0));
        voltageToDistanceCm.put(new InterpolatingDouble(2.05), new InterpolatingDouble(6.0));
        voltageToDistanceCm.put(new InterpolatingDouble(2.35), new InterpolatingDouble(5.0));
        voltageToDistanceCm.put(new InterpolatingDouble(2.72), new InterpolatingDouble(4.0));
        voltageToDistanceCm.put(new InterpolatingDouble(3.10), new InterpolatingDouble(3.0));
    }

    /**
     * Returns a linearized distance measurement, in centimeters.
     */
    public double get() {
        return voltageToDistanceCm.getInterpolated(new InterpolatingDouble(distanceSensor.getVoltage())).value;
    }
}
