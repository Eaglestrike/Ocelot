package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.settings.RobotSettings;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private ControlMode controlMode;

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

        double targetPos = RobotSettings.ENCODER_TICKS * height / RobotSettings.HEIGHT_TO_ENCODER_TICKS_RATIO;
        masterTalon.set(ControlMode.MotionMagic, targetPos);

        //TODO: only count a switch pressed if it's pressed for a certain time
        //TODO: Use PID to spin the encoder to the right spot
        //TODO: Find the Talon function that spins the motor while using the Encoder to measure rotations
    }

    //get the height in feet
    public double getHeight() {
        int encoderTicks = masterTalon.getSelectedSensorPosition(0);
        return encoderTicks * RobotSettings.HEIGHT_TO_ENCODER_TICKS_RATIO;
    }

    //increment can be negative, and in that case it would be a decrement
    //increment is measured in feet
    public void incrementHeight(double increment) {
        double currentHeight = getHeight();
        if (increment > 0 &&
                (currentHeight + increment <= RobotSettings.LIFT_HEIGHT)) {
            goToHeight(getHeight() + increment);
        }
        else throw new RuntimeException("The increment was out of range.");
    }
}