package org.team114.ocelot.settings;

public final class Settings {
    private Settings() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    //TODO: find out the real ratio
    public static final double CLIMBER_FEET_PER_REVOLUTION = 1;
    //TODO: find out the real lift height
    public static final int MAX_LIFT_HEIGHT = 1;

    public static final int ENCODER_TICKS_PER_REVOLUTION = 4096;

    public static final double TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE = 5;
    public static final int PNEUMATIC_PRESSURE_SENSOR_ID = 0;

    public static final int MAX_NUMBER_OF_CHANNELS = 16;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double CLIMBING_TIME = 10;
    public static final double GAME_TIME = 180;

    public static final class Drive {
        private Drive() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        public static final double WHEELBASE_WIDTH_FT = 2.6233;
        public static final double WHEEL_DIAMETER_FT = 0.6666;

    }

    public static final class GearShifter {
        private GearShifter() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        public static final int HIGH_GEAR_CHANNEL = 0;
        public static final int LOW_GEAR_CHANNEL = 1;
    }

    public static final class DriveSide {
        private DriveSide() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        public static final int LEFT_MASTER = 1;
        public static final int LEFT_SLAVE = 2;

        public static final int RIGHT_MASTER = 10;
        public static final int RIGHT_SLAVE = 9;
    }

    public static final class CheesyDriveHelper {
        private CheesyDriveHelper() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        //idle range (-0.02, 0.02) // range (-1.0,1.0)
        public static final double THROTTLE_DEADBAND = 0.02;

        //idle range (-0.02, 0.02) // range (-1.0,1.0)
        public static final double WHEEL_DEADBAND = 0.02;

        public static final double TURN_SENSITIVITY = 1.0;
    }
}
