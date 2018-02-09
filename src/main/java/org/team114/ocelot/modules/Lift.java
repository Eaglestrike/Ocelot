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
    private final DigitalInput bottomLimitSwitch;

    private double goalHeight;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput topLimitSwitch,
                DigitalInput bottomLimitSwitch) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;

        this.topLimitSwitch = topLimitSwitch;
        this.bottomLimitSwitch = bottomLimitSwitch;

        this.masterTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        this.masterTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        this.slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());
    }

    /**
     * Set the setpoint of the lift
     * @param height measured in feet
     */
    public void goToHeight(double height) {
        // as if the limit switches are wired to the talon
        if (topLimitSwitch.get()) {
            masterTalon.setSelectedSensorPosition(convertFeetToTicks(Settings.MAX_LIFT_HEIGHT), 0, 0);
        } else if (bottomLimitSwitch.get()) {
            masterTalon.setSelectedSensorPosition(0, 0, 0);
        }

        masterTalon.set(ControlMode.MotionMagic, convertFeetToTicks(height));
        goalHeight = height;
    }

    //get the height in feet
    public double getHeight() {
        return convertTicksToFeet(masterTalon.getSelectedSensorPosition(0));
    }

    /**
     * Shift the setpoint of the lift
     * @param increment can be negative, measured in feet
     */
    public void incrementHeight(double increment) {
        goalHeight += increment;
        if (goalHeight > Settings.MAX_LIFT_HEIGHT) {
            goalHeight = Settings.MAX_LIFT_HEIGHT;
        }
        else if (goalHeight < 0) {
            goalHeight = 0;
        }
        goToHeight(goalHeight);
    }

    private static double convertTicksToFeet(int ticks) {
        double revolutions = ticks / Settings.Drive.ENCODER_TICKS_PER_REVOLUTION;
        double feet = revolutions * Settings.CLIMBER_FEET_PER_REVOLUTION;
        return feet;
    }

    private static int convertFeetToTicks(double feet) {
        double revolutions = feet / Settings.CLIMBER_FEET_PER_REVOLUTION;
        double ticks = revolutions * Settings.Drive.ENCODER_TICKS_PER_REVOLUTION;
        return (int) ticks;
    }
}
