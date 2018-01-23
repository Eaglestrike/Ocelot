package org.team114.ocelot.subsystems.drive;

public final class SetSideSpeedEvent extends DriveEvent {

    private final double leftspeed;
    private final double rightspeed;

    public SetSideSpeedEvent(double leftspeed, double rightspeed) {
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;
    }

    public double getLeftspeed() {
        return leftspeed;
    }

    public double getRightspeed() {
        return rightspeed;
    }
}
