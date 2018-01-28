package org.team114.ocelot.settings;

public final class RobotSettings {
    private RobotSettings() {
        throw new AssertionError();
    }

    public static final double WHEELBASE_WIDTH_FT = 1.9;
    public static final double WHEEL_DIAMETER_FT = 2.0/3.0;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double MAX_CENTRI_ACCEL = 1;
}
