package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public final class SetSideSpeedEvent extends DriveEvent {

    public final double leftspeed;
    public final double rightspeed;

    public SetSideSpeedEvent(double leftspeed, double rightspeed) {
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;
    }
}
