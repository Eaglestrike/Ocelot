package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.team114.ocelot.event.Event;

public class SetNeutralModeEvent extends Event {

    public final NeutralMode neutralMode;

    public SetNeutralModeEvent(NeutralMode mode) {
        this.neutralMode = mode;
    }
}
