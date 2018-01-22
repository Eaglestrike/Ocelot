package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public final class SetControlModeEvent extends DriveEvent {

    public final ControlMode mode;

    public SetControlModeEvent(ControlMode mode) {
        this.mode = mode;
    }
}
