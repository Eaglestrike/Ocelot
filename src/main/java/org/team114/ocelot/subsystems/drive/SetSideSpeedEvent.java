package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public final class SetSideSpeedEvent extends DriveEvent {

    public final ControlMode mode;
    public final double leftspeed;
    public final double rightspeed;

    public SetSideSpeedEvent(ControlMode mode, double leftspeed, double rightspeed) {
        this.mode = mode;
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;
    }
}