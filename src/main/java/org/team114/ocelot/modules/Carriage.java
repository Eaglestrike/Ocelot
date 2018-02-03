package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Carriage {
    private final Solenoid leftIntake;
    private final Solenoid rightIntake;
    private final Solenoid rightLift1;
    private final Solenoid rightLift2;
    private final Solenoid leftLift1;
    private final Solenoid leftLift2;
    private final Ultrasonic leftDistanceSensor;
    private final Ultrasonic rightDistanceSensor;
    private final TalonSRX leftTalon;
    private final TalonSRX rightTalon;


    public Carriage(Solenoid leftMech, Solenoid rightMech, Solenoid _rightLift1, Solenoid _rightLift2, Solenoid _leftLift1,
                    Solenoid _leftLift2, Ultrasonic _leftDistanceSensor, Ultrasonic _rightDistanceSensor, TalonSRX left, TalonSRX right) {
        leftIntake = leftMech;
        rightIntake = rightMech;
        rightLift1 = _rightLift1;
        rightLift2 = _rightLift2;
        leftLift1 = _leftLift1;
        leftLift2 = _leftLift2;
        leftDistanceSensor = _leftDistanceSensor;
        rightDistanceSensor = _rightDistanceSensor;
        leftTalon = left;
        rightTalon = right;

    }

    public double getLeftDistanceInFeet() {
        double range = 12 * leftDistanceSensor.getRangeInches();
        return range;
    }

    public double getRightDistanceInFeet() {
        double range = 12 * rightDistanceSensor.getRangeInches();
        return range;
    }

    public void actuateIntake(boolean actuate) {
        leftIntake.set(actuate);
        rightIntake.set(actuate);
    }

    public void actuateFirstLift(boolean actuate) {
        rightLift1.set(actuate);
        rightLift2.set(actuate);
    }

    public void actuateSecondLift(boolean actuate) {
        leftLift1.set(actuate);
        leftLift2.set(actuate);
    }

    public void rotateLeft() {
        leftTalon.set(ControlMode.Velocity, -1);
    }

    public void rotateRight() {
        rightTalon.set(ControlMode.Velocity, 1);
    }

    public void stopLeft() {
        leftTalon.set(ControlMode.Velocity, 0);
    }

    public void stopRight() {
        rightTalon.set(ControlMode.Velocity, 0);
    }

}
