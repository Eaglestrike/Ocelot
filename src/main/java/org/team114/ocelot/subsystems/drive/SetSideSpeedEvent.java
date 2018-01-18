package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import org.team114.ocelot.event.Event;

public class SetSideSpeedEvent {

    public final ControlMode mode;
    public final double leftspeed;
    public final double rightspeed;

    public SetSideSpeedEvent(ControlMode mode, double leftspeed, double rightspeed) extends Event {
        this.mode = mode;
        this.leftspeed = leftspeed;
        this.rightspeed = rightspeed;
    }
}
