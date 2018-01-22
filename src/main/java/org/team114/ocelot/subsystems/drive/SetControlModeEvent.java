package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public final class SetControlModeEvent extends DriveEvent {

    public final ControlMode mode;
    public final Side side;

    public SetControlModeEvent(Side side, ControlMode mode) {
        this.side = side;
        this.mode = mode;
    }
}
