package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;

public interface AbstractSuperstructure extends Subsystem {
    /**
     * Returns the height of the lift, in feet.
     */
    double getHeight();

    /**
     * Shift the setpoint of the lift, in feet.
     * @param increment can be negative.
     */
    void incrementHeight(double increment);

    /**
     * Opens or closes the carriage
     * @param open
     */
    void actuateCarriage(boolean open);

    /**
     * Spins the wheels on the carriage if spin is true
     * @param spin
     */
    void spinCarriage(boolean spin);

    /**
     * Lower the carriage if dropped is true
     * @param dropped
     */
    void dropCarriage(boolean dropped);
}
