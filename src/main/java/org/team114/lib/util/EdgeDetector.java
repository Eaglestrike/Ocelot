package org.team114.lib.util;

import java.util.concurrent.Callable;

/**
 * Detector for changes in the status of a boolean function.
 */
public class EdgeDetector {
    /**
     * Enum representing the type of change.
     */
    public enum EdgeType {
        /**
         * True -&gt; false.
         */
        FALLING,
        /**
         * False -&gt; true.
         */
        RISING,
        /**
         * No change.
         */
        FLAT
    }

    private Callable<Boolean> lambda;
    private boolean lastValue = false;

    /**
     * Creates an edge detector from a function to track.
     * @param lambda boolean function to detect changes in
     */
    public EdgeDetector(Callable<Boolean> lambda) {
        this.lambda = lambda;
    }

    /**
     * Updates the latest known value by calling the lambda.
     * @return new lastValue
     */
    public boolean update() {
        try {
            lastValue = lambda.call();
        } catch (Exception e) {
            lastValue = false;
        }
        return lastValue;
    }

    /**
     * Determines the type of change in the tracked function.
     * @return an edge type representing the change
     */
    public EdgeType getEdge() {
        boolean newValue;
        try {
            newValue = lambda.call();
        } catch (Exception e) {
            newValue = false;
        }

        boolean lastValue = this.lastValue;
        this.lastValue = newValue;

        // true -> false
        if (lastValue && !newValue) {
            return EdgeType.FALLING;
        }

        // false -> true
        if (!lastValue && newValue) {
            return EdgeType.RISING;
        }

        // (false -> false) | (true -> true)
        return EdgeType.FLAT;
    }

    /**
     * Checks if the function is falling.
     * @return  whether or not the tracked function has changed from true to false
     */
    public boolean falling() {
        return getEdge() == EdgeType.FALLING;
    }

    /**
     * Checks if the function is rising.
     * @return whether or not the tracked function has changed from false to true
     */
    public boolean rising() {
        return getEdge() == EdgeType.RISING;
    }

    /**
     * Checks if the function is flat.
     * @return whether or not the tracked function is stable
     */
    public boolean flatlining() {
        return getEdge() == EdgeType.FLAT;
    }
}
