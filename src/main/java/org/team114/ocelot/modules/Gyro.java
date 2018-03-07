package org.team114.ocelot.modules;

/**
 * A gyroscope.
 * All angles are CCW.
 */
public interface Gyro {
    /**
     * Returns the heading according the gyroscope in radians.
     * @return heading of the gyroscope in radians
     */
    double getYaw();

    /**
     * Returns the heading according the gyroscope in degrees.
     * @return heading of the gyroscope in degrees
     */
    double getYawDegrees();

    /**
     * Runs initial setup for the gyroscope.
     * After this method is run, the gyro points at 90 degrees.
     */
    void init();
}
