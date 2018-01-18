package org.team114.ocelot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Class providing access to SmartDashboard, and abstracting away string keys keys.
 * Usage:
 * <pre> {@code
 *      new DashboardHandle(<KEY>).put(<VALUE>);
 *  }</pre>
 */
public class DashboardHandle  {
    private final static Set<String> keys = new HashSet<>();

    /**
     * The string used to access SmartDashboard.
     */
    private String key;

    public DashboardHandle(String key) throws IllegalAccessError {
        if (!keys.add(key))
            throw new IllegalAccessError("Handle to key already exists");
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

    public void put(String[] values) {
        SmartDashboard.putStringArray(key, values);
    }

    public void put(double[] values) {
        SmartDashboard.putNumberArray(key, values);
    }

    public void put(boolean[] values) {
        SmartDashboard.putBooleanArray(key, values);
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
