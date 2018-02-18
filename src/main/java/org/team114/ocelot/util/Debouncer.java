package org.team114.ocelot.util;

import edu.wpi.first.wpilibj.Timer;

public class Debouncer {

    private final double refreshTime;
    private boolean value = false;
    private double lastTime = Double.NEGATIVE_INFINITY;

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

    public boolean debounce(boolean current) {
        return debounce(current, Timer.getFPGATimestamp());
    }

}
