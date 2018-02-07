package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.settings.RobotSettings;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private ControlMode controlMode;
    private double goalHeight;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput topLimitSwitch,
                DigitalInput bottomLimitSwitch) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;
//        masterTalon.configMotionCruiseVelocity();
//        masterTalon.configMotionAcceleration();
//        masterTalon.setSelectedSensorPosition(0, );
    }

    //go to height (height in feet)
    // multiply height by constant
    public void goToHeight(double height) {
        //TODO: think about limit switches (digital inputs)
        //TODO: only count a switch pressed if it's pressed for a certain time

        masterTalon.set(ControlMode.MotionMagic, convertFeettoTicks(height));
        goalHeight = 0;
    }

    //get the height in feet
    public double getHeight() {
        return convertTickstoFeet(masterTalon.getSelectedSensorPosition(0));
    }

    //increment can be negative, and in that case it would be a decrement
    //increment is measured in feet
    public void incrementHeight(double increment) {
        goalHeight += increment;
        if (goalHeight > RobotSettings.LIFT_HEIGHT) {
            goalHeight = RobotSettings.LIFT_HEIGHT;
        }
        else if (goalHeight < 0) {
            goalHeight = 0;
        }
        goToHeight(goalHeight);
    }

    public double convertTickstoFeet(double ticks) {
        return ticks * RobotSettings.FEET_TO_ENCODER_TICKS_RATIO / RobotSettings.ENCODER_TICKS;
    }

    public double convertFeettoTicks(double feet) {
        return RobotSettings.ENCODER_TICKS * feet / RobotSettings.FEET_TO_ENCODER_TICKS_RATIO;
    }
}
