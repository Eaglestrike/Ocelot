package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public final class SetNeutralModeEvent extends DriveEvent {

    public final NeutralMode neutralMode;

    public SetNeutralModeEvent(NeutralMode mode) {
        this.neutralMode = mode;
    }
}