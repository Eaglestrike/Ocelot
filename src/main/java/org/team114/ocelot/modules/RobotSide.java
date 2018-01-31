package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.RobotSettings;
import org.team114.ocelot.util.Side;

import java.util.Arrays;
import java.util.List;

/**
 * This class encapsulates one side of the drive motors.
 * This class is responsible for ensuring that the master and slave talons are linked.
 * Coupled with {@link Side}
 */
public class RobotSide {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;
    private ControlMode controlMode;
    private double lastSpeedSet;
    private NeutralMode neutralMode;

    public RobotSide(TalonSRX masterTalon, TalonSRX slaveTalon) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;
        slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());
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

    public int getEncoderTicks() {
        return masterTalon.getSelectedSensorPosition(0);
    }

    //TODO add actual encoders
    public double getEncoderDistance() {
        double ticks = (double) getEncoderTicks();
        double cirumference = Math.PI * RobotSettings.WHEEL_DIAMETER_FT;
        double ticksPerRotation = 4096;
        double gearRatio = 2;

        return (ticks * cirumference) / ticksPerRotation / gearRatio;
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
