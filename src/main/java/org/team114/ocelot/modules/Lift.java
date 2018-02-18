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

    private final DigitalInput topLimitSwitch;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput topLimitSwitch) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;

        this.topLimitSwitch = topLimitSwitch;

        this.masterTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        this.slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());

        masterTalon.configPeakCurrentLimit(30, 0);
        slaveTalon.configPeakCurrentLimit(30, 0);
    }

    /**
     * Checks all the limit switches and zeros encoders as (if) necessary.
     */
    public void zeroEncodersIfNecessary() {
        if (topLimitSwitch.get()) {
            masterTalon.setSelectedSensorPosition(convertFeetToTicks(Settings.Lift.MAX_HEIGHT), 0, 0);
        }
    }

    /**
     * Sets the setpoint of the lift.
     * @param height measured in feet
     */
    public void goToHeight(double height) {
        masterTalon.set(ControlMode.MotionMagic, convertFeetToTicks(height));
    }

    //get the height in feet
    public double getHeight() {
        return convertTicksToFeet(masterTalon.getSelectedSensorPosition(0));
    }

    private static double convertTicksToFeet(int ticks) {
        double revolutions = ticks / Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        double feet = revolutions * Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        return feet;
    }

    private static int convertFeetToTicks(double feet) {
        if(feet * 12 > 5) {
            feet = feet / 2 + 5 / 24;
        }
        double revolutions = feet / Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        double ticks = revolutions * Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        return (int) ticks;
    }
}
