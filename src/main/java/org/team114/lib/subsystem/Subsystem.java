package org.team114.lib.subsystem;

/**
 * Interface which must be implemented by all subsystems.
 * @see SubsystemManager
 */
public interface Subsystem {
    /**
     * Runs on subsystem startup. This method should be used for setup that needs to be run immediately
     * before the step begins. Other setup should go in the constructor.
     * @param timestamp the current uptime
     */
    void onStart(double timestamp);

    /**
     * Runs on subsystem shutdown. This method should be used for any cleanup that is needed to return the
     * subsystem to its default state.
     * @param timestamp the current uptime
     */
    void onStop(double timestamp);

    /**
     * Runs every time the subsystem steps (that is, every
     * {@link org.team114.lib.subsystem.SubsystemManager#STEP_PERIOD} seconds). This method should work
     * towards accomplishing the goal set by the current control class.
     * @param timestamp the current uptime
     */
    void onStep(double timestamp);
}
