package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.Registry;
import org.team114.ocelot.util.Side;

import java.util.Arrays;
import java.util.List;

/**
 * This class encapsulates one side of the drive motors.
 * This class is responsible for ensuring that the master and slave talons are linked.
 * Coupled with {@link Side}
 */
public class DriveSide {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;
    private final Registry registry;
    private ControlMode controlMode;
    private double lastSpeedSet;
    private NeutralMode neutralMode;

    public DriveSide(Registry registry) {
        this.registry = registry;
        this.masterTalon = new TalonSRX(registry.getConfiguration().getInt("master"));
        this.slaveTalon = new TalonSRX(registry.getConfiguration().getInt("slave"));
        slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());
        masterTalon.setSelectedSensorPosition(0, 0, 0);
    }

    public void refresh() {
        this.masterTalon.set(this.getControlMode(), this.getLastSpeedSet());
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        this.neutralMode = neutralMode;
        this.masterTalon.setNeutralMode(this.neutralMode);
    }

    public NeutralMode getNeutralMode() {
        return neutralMode;
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
        refresh();
    }

    public double getLastSpeedSet() {
        return lastSpeedSet;
    }

    public void setSpeed(double speed) {
        this.lastSpeedSet = speed;
        refresh();
    }

    public int getPosition() {
        return masterTalon.getSelectedSensorPosition(0);
    }

    public double getVelocity() {
        return masterTalon.getSelectedSensorVelocity(0);
    }

    public TalonSRX getMasterTalon() {
        return masterTalon;
    }

    public TalonSRX getSlaveTalon() {
        return slaveTalon;
    }

    public List<? extends TalonSRX> getTalons() {
        return Arrays.asList(this.masterTalon, this.slaveTalon);
    }
}
