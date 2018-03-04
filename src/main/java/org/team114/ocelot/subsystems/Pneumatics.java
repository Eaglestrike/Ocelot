package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;

public interface Pneumatics extends Subsystem {
    /**
     * Returns the pressure of the pneumatic system
     */
    double getPressure();

    /**
     * Returns the state of the compressor (on/off).
     */
    boolean compressing();

    /**
     * Hands off controlling pressure to the compressor.
     * Re-enable by setting minimum pressure.
     */
    void unset();

    /**
     * Sets the point at which the pressure will turn on. The guarantee is that
     * the compressor will be on (hence conserving power) if the pressure is
     * below this point.
     * @param pressure the point which will trigger the compressor.
     */
    void setMinimumPressure(double pressure);

    /**
     * The psi above the minimum pressure at which the compressor will stop compressing.
     */
    void setPressureMargin(double margin);
}
