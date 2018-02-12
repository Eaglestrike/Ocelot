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
    public static final double FEET_PER_CENTIMETER = 0.0328084;

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

        // amps
        public static final int CURRENT_LIMIT_THRESHOLD = 40;
        public static final int CURRENT_LIMIT = 35;
        public static final int CURRENT_LIMIT_DURATION_MS = 200;
    }

    public static final class Lift {
        private Lift() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int MASTER = -1;
        public static final int SLAVE = -1;
        public static final int TOP_LIMIT_SWITCH = -1;
    }

    public static final class Carriage {
        private Carriage() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int INTAKE_CHANNEL = -1;
        public static final int LIFT_STAGE_ONE = -1;
        public static final int LIFT_STAGE_TWO = -1;
        public static final int LEFT_SPINNER = -1;
        public static final int RIGHT_SPINNER = -1;
    }

    public static final class DistanceSensor {
        private DistanceSensor() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int CHANNEL = -1;
        // centimeters
        public static final double MAX_DISTANCE = 30;
        public static final double MIN_DISTANCE = 4;
        // volts
        public static final double MAX_VOLTAGE = 0.3;
        public static final double MIN_VOLTAGE = 3.1;
    }

    public static final class GearShifter {
        private GearShifter() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int HIGH_GEAR = 0;
        public static final int LOW_GEAR = 1;
    }

    public static final class CheesyDriveHelper {
        private CheesyDriveHelper() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final double TURN_SENSITIVITY = 1.0;
        public static final double THROTTLE_GROWTH = 0.65;
        public static final double WHEEL_GROWTH = 0.5;
    }
}
