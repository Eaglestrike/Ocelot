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

    //go to height (height in feet)
    // multiply height by constant
    public void goToHeight(double height) {
        //TODO: only count a switch pressed if it's pressed for a certain time

        //as if the limit switches are wired to the talon

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

    //increment can be negative, and in that case it would be a decrement
    //increment is measured in feet
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

    public double convertTicksToFeet(double ticks) {
        return (ticks / RobotSettings.ENCODER_TICKS_PER_REVOLUTION) / RobotSettings.CLIMBER_FEET_PER_REVOLUTION;
    }

    public int convertFeetToTicks(double feet) {
        return (int)(RobotSettings.ENCODER_TICKS_PER_REVOLUTION * (feet / RobotSettings.CLIMBER_FEET_PER_REVOLUTION));
    }
}
