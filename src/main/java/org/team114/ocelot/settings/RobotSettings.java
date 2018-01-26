package org.team114.ocelot.settings;

public final class RobotSettings {
    private RobotSettings() {
        throw new AssertionError();
    }

    public static final double WHEELBASE_WIDTH = 0.55245;
    public static final double WHEEL_DIAMETER = 0.1016;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double MAX_CENTRI_ACCEL = 1;
}
