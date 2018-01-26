package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.team114.ocelot.util.Side;

public interface AbstractDrive {
    void setSideSpeed(Side sides, double speed);
    void setControlMode(Side sides, ControlMode controlMode);
    void setNeutralMode(Side sides, NeutralMode neutralMode);

    void selfTest();
}
