package org.team114.ocelot.settings;

public final class Settings {
    private Settings() {
    }

    //TODO: find out the real ratio
    public static final double CLIMBER_FEET_PER_REVOLUTION = 1;
    //TODO: find out the real lift height
    public static final int MAX_LIFT_HEIGHT = 1;

    public static final int ENCODER_TICKS_PER_REVOLUTION = 4096;

    public static double TYPICAL_PNEUMATIC_SUPPLY_VOLTAGE = 5;
    public static int PNEUMATIC_PRESSURE_SENSOR_ID = 0;

    public static final int MAX_NUMBER_OF_CHANNELS = 16;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double CLIMBING_TIME = 10;
    public static final double GAME_TIME = 180;
}
