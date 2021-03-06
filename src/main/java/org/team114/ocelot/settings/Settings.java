package org.team114.ocelot.settings;

public final class Settings {
    private Settings() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    // misc
    public static final double TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE = 5;
    public static final double GAME_TIME = 180;
    public static final double CLIMBING_TIME_ESTIMATE = 10;
    public static final double FEET_PER_CENTIMETER = 0.0328084;
    public static final int TALON_CONFIG_TIMEOUT_MS = 10;

    // Inner classes for Modules and Subsystems

    public static final class Drive {
        private Drive() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }

        public static final double WHEELBASE_WIDTH_FT = 2.6233;
        public static final int ENCODER_TICKS_PER_REVOLUTION = 4096;
        // x ticks -> * rev/tick  * sprocket/rev  * 0.5PI ft / sprocket  = y ft
        // empirically determined
        public static final double DRIVE_ENCODER_FEET_PER_TICK = (284.5 / 12) / 156_439;

        public static final double DRIVE_ENCODER_TICKS_PER_FOOT = ENCODER_TICKS_PER_REVOLUTION *
                (60.0 / 24.0) /
                (0.5 * Math.PI);
    }

    public static final class PurePursuit {
        private PurePursuit() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final double CRUISE_VELOCITY = 10.0;
        public static final double DISTANCE_DECAY_CONSTANT = 1.4;
        public static final double MIN_SPEED = 2.4;
    }

    public static final class Pneumatics {
        private Pneumatics() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int PNEUMATIC_PRESSURE_SENSOR_ID = 0;
        public static final double DEFAULT_PRESSURE_MARGIN = 60;
    }

    public static final class Controller {
        private Controller() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final double STANDARD_DEADBAND = 0.2;
        public static final double FREER_DEADBAND = 0.5;
    }

    public static final class DriveSide {
        private DriveSide() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int LEFT_MASTER = 1;
        public static final int LEFT_SLAVE = 2;
        public static final int RIGHT_MASTER = 10;
        public static final int RIGHT_SLAVE = 9;

        public static final int LOW_GEAR_VEL_PID_IDX = 0;

        // amps
        public static final int CURRENT_LIMIT_THRESHOLD = 60;
        public static final int CURRENT_LIMIT = 50;
        public static final int CURRENT_LIMIT_DURATION_MS = 200;
    }

    public static final class Lift {

        private Lift() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int MASTER = 4;
        public static final int SLAVE = 6;

        public static final int MAX_HEIGHT_TICKS = 35_000; // CAD estimate from Kat
        public static final double CLIMBER_FEET_PER_REVOLUTION = 3.63168 / 12; // CAD estimate from Albert
        //TODO tune
        public static final int NORMAL_SPEED = 100;
        public static final int ENCODER_TICKS_PER_REVOLUTION = 4096;

        // amps
        public static final int CURRENT_LIMIT_THRESHOLD = 27;
        public static final int CURRENT_LIMIT = 23;
        public static final int CURRENT_LIMIT_DURATION_MS = 500;

        public static final int MAGIC_PID_SLOT_INDEX = 0;
        public static final int MAGIC_PID_LOOP_INDEX = 0;
    }

    public static final class Carriage {
        private Carriage() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final double OUTTAKE_COMMAND_FAST = -1.0;
        public static final double OUTTAKE_COMMAND_NORMAL = -0.5;
        public static final double OUTTAKE_COMMAND_SLOW = -0.25;
        public static final double OUTTAKE_TIME_SECONDS = 0.5;

        public static final double OUTTAKE_COMMAND_RISING_OPEN_IDLE = -0.3;
        public static final int INTAKE_CHANNEL = 7;
        public static final int LIFT_STAGE_ONE = 2;
        public static final int LIFT_STAGE_TWO = 3;
        public static final int LEFT_SPINNER = 7;
        public static final int RIGHT_SPINNER = 8;
        public static final double INTAKE_IN_COMMAND = 0.5;
        //TODO tune these two
        public static final double INTAKE_IN_LOW_VOLTAGE_COMMAND = 3.5 / 12.0;
        public static final double BOX_DISTANCE_INTAKE_THRESHOLD_CM = 20.0;
    }

    public static final class SuperStructure {
        public static final int COMPLETE_ACTION_THRESHOLD_TICKS = 100;
        public static final int ZEROING_INCREMENT_TICKS = -250;
        public static final int AUT0_SCALE_HEIGHT_TICKS = 32_000;
        public static final int AUTO_SWITCH_HEIGHT_TICKS = 15_000;
    }

    public static final class DistanceSensor {
        private DistanceSensor() {
            throw new AssertionError("Constructor must not be called on utility class.");
        }
        public static final int CHANNEL = 1;
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
