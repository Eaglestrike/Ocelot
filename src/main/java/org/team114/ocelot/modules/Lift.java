package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private ControlMode controlMode;
    private double goalHeight;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;
    }

    //go to height (height in feet)
    // multiply height by constant
    public void goToHeight(double height) {
        //TODO: think about limit switches (digital inputs)
        //TODO: only count a switch pressed if it's pressed for a certain time
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
        if (goalHeight > Settings.MAX_LIFT_HEIGHT) {
            goalHeight = Settings.MAX_LIFT_HEIGHT;
        }
        else if (goalHeight < 0) {
            goalHeight = 0;
        }
        goToHeight(goalHeight);
    }

    public double convertTicksToFeet(double ticks) {
        return (ticks / Settings.ENCODER_TICKS_PER_REVOLUTION) / Settings.CLIMBER_FEET_PER_REVOLUTION;
    }

    public double convertFeetToTicks(double feet) {
        return Settings.ENCODER_TICKS_PER_REVOLUTION * (feet / Settings.CLIMBER_FEET_PER_REVOLUTION);
    }
}
