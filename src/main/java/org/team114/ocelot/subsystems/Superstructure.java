package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.modules.Carriage;

public interface Superstructure extends Subsystem {
    /**
     * Returns the height of the lift.
     * @return height of the lift in ticks
     */
    int getHeight();

    /**
     * Set the lift height.
     * @param setPoint goal height in ticks.
     */
    void setHeight(int setPoint);

    /**
     * Opens or closes the carriage.
     * @param open whether the carriage should be open or closed.
     */
    void actuateCarriage(boolean open);

    /**
     * Spins the wheels on the carriage if spin is true.
     * @param command percentage output for the carriage
     */
    void spinCarriage(double command);

    /**
     * Lift the carriage to the selected stage.
     * @param stage which height the carriage should go to
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

    void setOuttakeSpeed(double command);

    void setHeightFraction(double fraction);

    void setManualControl(double speed);
}
