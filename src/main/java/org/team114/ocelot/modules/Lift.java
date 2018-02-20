package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.settings.Settings;
import org.team114.ocelot.util.Debouncer;

import java.util.Set;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private final DigitalInput midLimitSwitch;
    private final Debouncer middleDebouncer;


    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput midLimitSwitch) {
        this.midLimitSwitch = midLimitSwitch;
        middleDebouncer = new Debouncer(Settings.Lift.DEBOUNCER_REFRESH);

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
     */
    public void zeroEncodersIfNecessary() {
        //upper zero
        if (masterTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
            masterTalon.setSelectedSensorPosition(convertFeetToTicks(Settings.Lift.MAX_HEIGHT), 0, 0);
            return;
        }

        //lower zero
        if (masterTalon.getSensorCollection().isRevLimitSwitchClosed()) {
            //TODO is it really zero?
            masterTalon.setSelectedSensorPosition(convertFeetToTicks(0), 0, 0);
            return;
        }
    }

    /**
     * Sets the setpoint of the lift.
     * @param height measured in feet
     */
    public void goToHeight(double height) {
        if (!middleDebouncer.debounce(midLimitSwitch.get())) {
            height = height / 2 + 5 / 24;
        }

        masterTalon.set(ControlMode.MotionMagic, convertFeetToTicks(height));
    }

    //get the height in feet
    public double getHeight() {
        return convertTicksToFeet(masterTalon.getSelectedSensorPosition(0));
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
