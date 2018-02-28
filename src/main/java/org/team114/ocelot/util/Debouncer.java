package org.team114.ocelot.util;

import edu.wpi.first.wpilibj.Timer;

public class Debouncer {

    private final double refreshTime;
    private boolean value = false;
    private double lastTime = Double.NEGATIVE_INFINITY;

    /**
     * Constructs a Debouncer.
     * @param refreshTime how often a boolean must remain a value to change the value
     */
    public Debouncer(double refreshTime) {
        this.refreshTime = refreshTime;
    }

    public boolean debounce(boolean current, double timestamp) {
        if (current != value) {
            if (timestamp - lastTime >= refreshTime) {
                lastTime = timestamp;
                value = current;
            }
        } else {
            lastTime = timestamp;
        }
        return value;
    }

    /**
     * Same as {@link #debounce(boolean, double)} but assumes seconds from getFPGATimestamp.
     * @param current boolean value
     * @return the debounced value
     */
    public boolean debounce(boolean current) {
        return debounce(current, Timer.getFPGATimestamp());
    }

}
