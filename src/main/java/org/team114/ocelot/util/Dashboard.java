package org.team114.ocelot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javax.annotation.Nullable;

/**
 * Enum providing type-safe access to SmartDashboard. String keys are abstracted away.
 * Usage:
 * <pre> {@code
 *      Dashboard.<KEY>.put(<VALUE>)
 *  }</pre>
 */
public enum Dashboard {
    HEADING("heading"),
    VELOCITY_X("velocity x"),
    VELOCITY_Y("velocity y")
    ; // add keys above

    /**
     * The string key mapping the value to SmartDashboard.
     */
    private String key;

    Dashboard(String key) {
        this.key = key;
    }

    public void put(String value) {
        SmartDashboard.putString(key, value);
    }

    public void put(double value) {
        SmartDashboard.putNumber(key, value);
    }

    public void put(boolean value) {
        SmartDashboard.putBoolean(key, value);
    }

    @Nullable
    public String getString() {
        return SmartDashboard.getString(key, null);
    }

    @Nullable
    public Double getNumber() {
        // since we must give `getNumber` a default value, and it
        // is a primitive, this is the only way to check if it
        // should be null
        if (!SmartDashboard.containsKey(key))
            return null;
        return SmartDashboard.getNumber(key, Double.NaN);
    }

    @Nullable
    public Boolean getBoolean() {
        // since we must give `getBoolean` a default value, and it
        // is a primitive, this is the only way to check if it
        // should be null
        if (!SmartDashboard.containsKey(key))
            return null;
        return SmartDashboard.getBoolean(key, false);
    }

    @Nullable
    public String[] getStrings() {
        return SmartDashboard.getStringArray(key, null);
    }

    @Nullable
    public double[] getNumbers() {
        return SmartDashboard.getNumberArray(key, (double[])null);
    }

    @Nullable
    public boolean[] getBooleans() {
        return SmartDashboard.getBooleanArray(key, (boolean[])null);
    }
}
