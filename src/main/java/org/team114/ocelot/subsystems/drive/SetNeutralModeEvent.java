package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class SetNeutralModeEvent extends DriveEvent {

    public final NeutralMode neutralMode;

    public SetNeutralModeEvent(NeutralMode mode) {
        super(SetNeutralModeEvent.class.getCanonicalName());
        this.neutralMode = mode;
    }
}
