package org.team114.ocelot.settings;

public final class Settings {
    private Settings() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    //TODO: find out the real ratio
    //TODO: find out the real lift height
    //TODO: move to inner class
    public static final int MAX_LIFT_HEIGHT = 1;
    public static final double CLIMBER_FEET_PER_REVOLUTION = 1;

    // IDs and Channels
    public static final int MAX_NUMBER_OF_CHANNELS = 16;
    public static final int PNEUMATIC_PRESSURE_SENSOR_ID = 0;

    // misc
    public static final double TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE = 5;
    public static final double GAME_TIME = 180;
    public static final double CLIMBING_TIME_ESTIMATE = 10;

    // Inner classes for Modules and Subsystems
    public static final class Drive {
        private Drive() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        public static final double WHEELBASE_WIDTH_FT = 2.6233;
        public static final int ENCODER_TICKS_PER_REVOLUTION = 4096;
        // x ticks -> * rev/tick  * sprocket/rev  * 0.5PI ft / sprocket  = y ft
        public static final double DRIVE_ENCODER_FEET_PER_TICK =
                (1.0 / ENCODER_TICKS_PER_REVOLUTION) *
                        (24.0 / 60.0) *
                        (0.5 * Math.PI);
    }

    public static final class DriveSide {
        private DriveSide() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int LEFT_MASTER = 1;
        public static final int LEFT_SLAVE = 2;
        public static final int RIGHT_MASTER = 10;

        public static final int RIGHT_SLAVE = 9;
        public static final int DRIVE_CURRENT_LIMIT_THRESHOLD = 40;

        public static final int DRIVE_CURRENT_LIMIT = 35;
        public static final int DRIVE_CURRENT_LIMIT_DURATION_MS = 200;
    }

    public static final class GearShifter {
        private GearShifter() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int HIGH_GEAR_CHANNEL = 0;
        public static final int LOW_GEAR_CHANNEL = 1;
    }

    public static final class CheesyDriveHelper {
        private CheesyDriveHelper() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final double TURN_SENSITIVITY = 1.0;
    }
}
