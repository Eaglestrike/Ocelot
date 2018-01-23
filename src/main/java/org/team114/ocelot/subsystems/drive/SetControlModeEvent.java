package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public final class SetControlModeEvent extends DriveEvent {

    private final ControlMode controlMode;
    private final Side side;

    public SetControlModeEvent(Side side, ControlMode controlMode) {
        this.side = side;
        this.controlMode = controlMode;
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public Side getSide() {
        return side;
    }
}
