package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.settings.RobotSettings;

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
        masterTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
        masterTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    }

    /**
     * Set the setpoint of the lift
     * @param height measured in feet
     */
    public void goToHeight(double height) {
        // as if the limit switches are wired to the talon
        if (topLimitSwitch.get()) {
            masterTalon.setSelectedSensorPosition(convertFeetToTicks(RobotSettings.MAX_LIFT_HEIGHT), 0, 0);
        }
        else if (bottomLimitSwitch.get()) {
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
        if (goalHeight > RobotSettings.MAX_LIFT_HEIGHT) {
            goalHeight = RobotSettings.MAX_LIFT_HEIGHT;
        }
        else if (goalHeight < 0) {
            goalHeight = 0;
        }
        goToHeight(goalHeight);
    }

    private static double convertTicksToFeet(int ticks) {
        double revolutions = ticks / RobotSettings.ENCODER_TICKS_PER_REVOLUTION;
        double feet = revolutions * RobotSettings.CLIMBER_FEET_PER_REVOLUTION;
        return feet;
    }

    private static int convertFeetToTicks(double feet) {
        double revolutions = feet / RobotSettings.CLIMBER_FEET_PER_REVOLUTION;
        double ticks = revolutions * RobotSettings.ENCODER_TICKS_PER_REVOLUTION;
        return (int) ticks;
    }
}
