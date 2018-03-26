package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.auto.modes.CrossBaseLine;

import java.util.function.Supplier;

/**
 * Class that allows a user to select which autonomous mode to execute from the web dashboard.
 */
public class AutoModeSelector {
//
//    public static final String AUTO_OPTIONS_DASHBOARD_KEY = "Auto Options";
//    public static final String SELECTED_AUTO_MODE_DASHBOARD_KEY = "Selected Auto Mode";
//    private static class AutoModeCreator {
//
//        private final String dbName;
//        private final Supplier<AutoModeBase> supplier;
//
//        private AutoModeCreator(String dashboardName, Supplier<AutoModeBase> supplier) {
//            dbName = dashboardName;
//            this.supplier = supplier;
//        }
//    }
//
//    private static final AutoModeCreator defaultMode = new AutoModeCreator(
//            "AutoDetect Alliance Gear than Hopper Shoot",
//            () -> new CrossBaseLine());
//
//    private static final AutoModeCreator[] allModes = {
//            new AutoModeCreator("Cross Baseline", () -> new CrossBaseLine()),
//    };
//
//    public static void initAutoModeSelector() {
//        StringBuilder s = new StringBuilder("[\n");
//        for (AutoModeCreator c : allModes) {
//            s.append(c.dbName);
//            s.append(",\n");
//        }
//        s.append("]");
//        SmartDashboard.putString(AUTO_OPTIONS_DASHBOARD_KEY, s.toString());
//        SmartDashboard.putString(SELECTED_AUTO_MODE_DASHBOARD_KEY, defaultMode.dbName);
//    }
//
//    public static AutoModeBase getSelectedAutoMode() {
//        String selectedModeName = SmartDashboard.getString(
//                SELECTED_AUTO_MODE_DASHBOARD_KEY,
//                "NO SELECTED MODE!!!!");
//        for (AutoModeCreator mode : allModes) {
//            if (mode.dbName.equals(selectedModeName)) {
//                return mode.supplier.get();
//            }
//        }
//        DriverStation.reportError("Failed to select auto mode: " + selectedModeName, false);
//        return defaultMode.supplier.get();
//    }
}
