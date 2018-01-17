package org.team114.ocelot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javax.annotation.Nullable;
import javax.print.DocFlavor;
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
    public static Set<String> keys = new HashSet<>();

    /**
     * The string used to access SmartDashboard.
     */
    private String key;

    public DashboardHandle(String key) throws IllegalAccessException {
        if (!keys.add(key))
            throw new IllegalAccessException("Handle to key already exists");
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
}
