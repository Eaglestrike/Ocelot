package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import java.util.Arrays;
import java.util.List;

/**
 * This class encapsulates one side of the drive motors.
 * This class is responsible for ensuring that the master and slave talons are linked.
 * Coupled with {@link Side}
 */
public class DriveSideSettings {
    private final TalonSRX masterTalonSRX;
    private final TalonSRX slaveTalonSRX;
    private ControlMode controlMode;
    private double speed;
    private NeutralMode neutralMode;

    public DriveSideSettings(TalonSRX masterTalonSRX, TalonSRX slaveTalonSRX) {
        this.masterTalonSRX = masterTalonSRX;
        this.slaveTalonSRX = slaveTalonSRX;
        slaveTalonSRX.set(ControlMode.Follower, masterTalonSRX.getDeviceID());
    }
    public ControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
        refresh();
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        refresh();
    }

    public TalonSRX getMasterTalonSRX() {
        return masterTalonSRX;
    }

    public TalonSRX getSlaveTalonSRX() {
        return slaveTalonSRX;
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        this.neutralMode = neutralMode;
        this.masterTalonSRX.setNeutralMode(this.neutralMode);
    }

    public List<? extends TalonSRX> getTalonSRXs() {
        return Arrays.asList(this.masterTalonSRX, this.slaveTalonSRX);
    }

    public void refresh() {
        this.masterTalonSRX.set(this.getControlMode(), this.getSpeed());
    }
}