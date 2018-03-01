package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.settings.Settings;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private final DigitalInput midLimitSwitch;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput midLimitSwitch) {
        this.midLimitSwitch = midLimitSwitch;

        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;

        this.slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());

        this.masterTalon.setInverted(false);
        this.slaveTalon.setInverted(true);

        this.masterTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyOpen, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyOpen, Settings.TALON_CONFIG_TIMEOUT_MS);
    }

    /**
     * Checks all the limit switches and zeros encoders as (if) necessary.
     * @return whether zero-ing was done
     */
    public boolean zeroEncodersIfNecessary() {
        //lower zero
        if (masterTalon.getSensorCollection().isRevLimitSwitchClosed()) {
            //TODO is it really zero?
            masterTalon.setSelectedSensorPosition(0, 0, 0);
            return true;
        }

        //upper zero
        if (masterTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
            masterTalon.setSelectedSensorPosition(Settings.Lift.MAX_HEIGHT_TICKS, 0, 0);
            return true;
        }
        return false;
    }

    /**
     * Returns true if either of the forward or reverse limit switch is triggered.
     */
    public boolean isLimitSwitchTriggered() {
        return masterTalon.getSensorCollection().isRevLimitSwitchClosed() ||
                masterTalon.getSensorCollection().isFwdLimitSwitchClosed();
    }

    /**
     * Sets the setpoint of the lift.
     * @param height measured in ticks
     */
    public void goToHeight(int height) {
        masterTalon.set(ControlMode.MotionMagic, height);
    }

    //get the height in feet
    public int getHeight() {
        return masterTalon.getSelectedSensorPosition(0);
    }

    private static double convertTicksToFeet(int ticks) {
        double revolutions = (double)ticks / (double)Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        double feet = revolutions * Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        return feet;
    }

    private static int convertFeetToTicks(double feet) {
        double revolutions = feet / Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        double ticks = revolutions * (double)Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        return (int)ticks;
    }
}
