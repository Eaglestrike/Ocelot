package org.team114.ocelot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Side;

public interface AbstractDrive extends Subsystem {
    void setSideSpeed(Side sides, double speed);
    void setControlMode(Side sides, ControlMode controlMode);
    void setNeutralMode(Side sides, NeutralMode neutralMode);
    void setDriveSignal(DriveSignal signal);

    void selfTest();
}
