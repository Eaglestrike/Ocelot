package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.event.Event;

public class SetNeutralModeEventHandler implements EventHandler{

    Drive drive;

    public SetNeutralModeEventHandler(Drive drive) {
        this.drive = drive;
    }

    public void handle(SetNeutralModeEvent event) {
        TalonSRX[] talons = drive.getTalons();
        for (TalonSRX talon: talons) {
            talon.setNeutralMode(event.neutralMode);
        }
    }
}
