package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

public class DriveSide {
    private final TalonSRX master;
    private final TalonSRX slave;

    public DriveSide(TalonSRX master, TalonSRX slave) {
        this.master = master;
        this.slave = slave;

        slave.set(ControlMode.Follower, master.getDeviceID());
    }

    public void setInverted(boolean inverted) {
        master.setInverted(inverted);
        slave.setInverted(inverted);
    }

    public void configureForTeleop() {
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 50, 0);
    }

    public void configureForAuto() {
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 3, 0);
    }

    public void setPercentOutput(double percentage) {
        master.set(ControlMode.PercentOutput, percentage);
    }

    /**
     * Returns encoder position in feet.
     */
    public double getPosition() {
        return master.getSelectedSensorPosition(0) * Settings.Drive.DRIVE_ENCODER_FEET_PER_TICK;
    }

    /**
     * Returns encoder velocity in feet/sec.
     */
    public double getVelocity() {
        return master.getSelectedSensorVelocity(0) * Settings.Drive.DRIVE_ENCODER_FEET_PER_TICK;
    }
}