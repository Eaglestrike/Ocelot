package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import org.team114.ocelot.Robot;
import org.team114.ocelot.settings.RobotSettings;

public class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    private final DigitalInput topLimitSwitch;
    private final DigitalInput bottomLimitSwitch;

    private double encoderTicks;

    public Lift(TalonSRX masterTalon, TalonSRX slaveTalon, DigitalInput topLimitSwitch,
                DigitalInput bottomLimitSwitch) {
        this.masterTalon = masterTalon;
        this.slaveTalon = slaveTalon;
        this.topLimitSwitch = topLimitSwitch;
        this.bottomLimitSwitch = bottomLimitSwitch;
        this.encoderTicks = 0;
    }

    //go to height (height in feet)
    // multiply height by constant
    public void goToHeight(double height) {
//        double encoderTicks = height / RobotSettings.HEIGHT_TO_ENCODER_TICKS_RATIO;
        if (topLimitSwitch.get() || bottomLimitSwitch.get()) {
            return;
        }
        //TODO: Use PID to spin the encoder to the right spot
        //TODO: Find the Talon function that spins the motor while using the Encoder to measure rotations
    }


    //get the height ()
    //return encoder ticks multiplied by the same constant reversed

    //get the height in feet
    public double getHeight() {
        return encoderTurns * RobotSettings.HEIGHT_TO_ENCODER_TICKS_RATIO;
    }

    //TODO: use goToHeight of the elevator height
    public void liftCompletely() { }

    //TODO: use goToHeight of the bottom from the current height
    public void dropCompletely() { }


}


