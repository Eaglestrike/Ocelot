package org.team114.ocelot.util;

/**
 * A drivetrain command consisting of the left, right motor settings and whether the brake controlMode is enabled.
 */
public class DriveSignal {
    public static final DriveSignal COAST = new DriveSignal(0, 0);
    public static final DriveSignal BRAKE = new DriveSignal(0, 0, true);

    private final double leftMotor;
    private final double rightMotor;
    private final boolean brakeMode;

    public DriveSignal(double left, double right) {
        this(left, right, false);
    }

    public DriveSignal(double left, double right, boolean brakeMode) {
        leftMotor = left;
        rightMotor = right;
        this.brakeMode = brakeMode;
    }

    public double getLeft() {
        return leftMotor;
    }

    public double getRight() {
        return rightMotor;
    }

    public boolean getBrakeMode() {
        return brakeMode;
    }

    @Override
    public String toString() {
        return "L: " + leftMotor + ", R: " + rightMotor + (brakeMode ? ", BRAKE" : "");
    }
}
