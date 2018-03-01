package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.modules.Carriage;

public interface SuperstructureInterface extends Subsystem {
    /**
     * Returns the height of the lift, in feet.
     */
    int getHeight();

    /**
     * Shift the setpoint of the lift, in feet.
     */
    void incrementHeight(int increment);

    /**
     * Set the lift height
     */
    void setHeight(int setPoint);

    /**
     * Opens or closes the carriage
     */
    void actuateCarriage(boolean open);

    /**
     * Spins the wheels on the carriage if spin is true.
     */
    void spinCarriage(double command);

    /**
     * Lift the carriage to the selected stage.
     */
    void actuateIntakeLift(Carriage.ElevationStage stage);


    /**
     * Configures the carriage for intaking boxes
     * Open and Motors running
     */
    void setWantIntake();

    /**
     * Configures the carriage to be closed
     * Open and Motors running in if there is a box
     */
    void setWantClosed();

    /**
     * Configures the carriage to idle in the open position
     * Open and Motors off
     */
    void setWantOpenIdle();

    /**
     * Configures the carriage for outtaking boxes
     * Closed and motors running OUT quickly
     * This will, after a few moments, effectively call setWantClosed();
     */
    void setWantClosedOuttaking();

    /**
     * Configures the carriage for intaking boxes
     * Open and Motors running
     */
    void setWantZero();

    /**
     * Moves the lift to the height of the scale for outtaking
     */
    void setWantScaleHeight();
    /**
     * Moves the lift to the low height for intaking
     */
    void setWantLowHeight();

    /**
     * Moves the lift to the height of the switch for outtaking
     */
    void setWantSwitchHeight();
}
