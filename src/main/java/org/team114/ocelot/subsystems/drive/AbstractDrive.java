package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.Side;
import org.team114.ocelot.util.motion.PurePursuitController;

public interface AbstractDrive extends Subsystem {
    void setSideSpeed(Side sides, double speed);
    void setControlMode(Side sides, ControlMode controlMode);
    void setNeutralMode(Side sides, NeutralMode neutralMode);
    void setDriveSignal(DriveSignal signal);
    void setDriveArcCommand(PurePursuitController.DriveArcCommand arc);

    void selfTest();
}
