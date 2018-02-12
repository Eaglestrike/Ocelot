package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.modules.Carriage;

public interface SuperstructureInterface extends Subsystem {
    /**
     * Returns the height of the lift, in feet.
     */
    double getHeight();

    /**
     * Shift the setpoint of the lift, in feet.
     */
    void incrementHeight(double increment);

    /**
     * Opens or closes the carriage
     */
    void actuateCarriage(boolean open);

    /**
     * Spins the wheels on the carriage if spin is true.
     */
    void spinCarriage(boolean spin);

    /**
     * Lift the carriage to the selected stage.
     */
    void actuateCarriageLift(Carriage.ElevationStage stage);
}
