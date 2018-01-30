package org.team114.ocelot.settings;

public final class RobotSettings {
    private RobotSettings() {
        throw new AssertionError();
    }

    public static final double WHEELBASE_WIDTH_FT = 9;
    public static final double WHEEL_DIAMETER_FT = 2.0/3.0;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double MAX_CENTRI_ACCEL = 1;

    public static final int TALON_LEFT_1 = 1;
    public static final int TALON_LEFT_2 = 2;
    public static final int TALON_RIGHT_1 = 3;
    public static final int TALON_RIGHT_2 = 4;

    public static final int JOYSTICK_LEFT = 0;
    public static final int JOYSTICK_RIGHT = 1;
    public static final int JOYSTICK_PRIMARY = -1;
}
