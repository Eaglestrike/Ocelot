package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

/**
 * Represents a side of the drive train, and encapsulates the required talons/
 */
public class DriveSide {
    private final TalonSRX master;
    private final TalonSRX slave;

    /**
     * Constructs a drive side from talons/
     * @param master the master talon
     * @param slave the slave talon
     */
    public DriveSide(TalonSRX master, TalonSRX slave) {
        this.master = master;
        this.slave = slave;

        this.slave.set(ControlMode.Follower, master.getDeviceID());
        this.master.setNeutralMode(NeutralMode.Brake);
        this.slave.setNeutralMode(NeutralMode.Brake);

        this.master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Settings.DriveSide.LOW_GEAR_VEL_PID_IDX, 10);
        this.master.setSensorPhase(false);

        this.master.configNominalOutputForward(0, 10);
        this.master.configNominalOutputReverse(0, 10);
        this.master.configPeakOutputForward(1, 10);
        this.master.configPeakOutputReverse(-1, 10);

        this.master.config_kF(Settings.DriveSide.LOW_GEAR_VEL_PID_IDX, 0.279714286, 10);
        this.master.config_kP(0, 0.5, 10);
        this.master.config_kI(0, 0, 10);
        this.master.config_kD(0, 2.5, 10);
    }

    /**
     * Inverts or un-inverts control.
     * @param inverted whether control should be inverted or not
     */
    public void setInverted(boolean inverted) {
        master.setInverted(inverted);
        slave.setInverted(inverted);
    }

    public void configureForTeleop() {
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 25, 0);
    }

    public void configureForAuto() {
        master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 3, 0);
    }

    public void setPercentOutput(double percentage) {
        master.set(ControlMode.PercentOutput, percentage);
    }

    public void setVelocity(double velocity_units_per_100ms) {
        master.set(ControlMode.Velocity, velocity_units_per_100ms);
    }

    /**
     * Returns encoder position.
     * @return encoder position in feet
     */
    public double getPosition() {
        return master.getSelectedSensorPosition(0) * Settings.Drive.DRIVE_ENCODER_FEET_PER_TICK;
    }

    /**
     * Returns encoder velocity.
     * @return velocity in feet per second
     */
    public double getVelocity() {
        return master.getSelectedSensorVelocity(0) * Settings.Drive.DRIVE_ENCODER_FEET_PER_TICK;
    }
}