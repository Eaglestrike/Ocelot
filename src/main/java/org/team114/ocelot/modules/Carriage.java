package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Carriage {
    Solenoid left;
    Solenoid right;
    Solenoid rightLift1;
    Solenoid rightLift2;
    Solenoid leftLift1;
    Solenoid leftLift2;
    Ultrasonic distanceSensorLeft;
    Ultrasonic distanceSensorRight;
    TalonSRX leftTalon;
    TalonSRX rightTalon;


    public Carriage(int leftMechId, int rightMechId, int rightLift1Id, int rightLift2Id, int leftLift1Id,
                    int leftLift2Id, int digitalInputLeft, int digitalOutputLeft, int digitalInputRight, int digitalOutputRight, int leftTalonId, int rightTalonId) {
        left = new Solenoid(leftMechId);
        right = new Solenoid(rightMechId);
        rightLift1 = new Solenoid(rightLift1Id);
        rightLift2 = new Solenoid(rightLift2Id);
        leftLift1 = new Solenoid(leftLift1Id);
        leftLift2 = new Solenoid(leftLift2Id);
        distanceSensorLeft = new Ultrasonic(digitalOutputLeft, digitalInputLeft);
        distanceSensorRight = new Ultrasonic(digitalOutputRight, digitalInputRight);
        leftTalon = new TalonSRX(leftTalonId);
        rightTalon = new TalonSRX(rightTalonId);

    }

    public double getLeftDistanceInFeet() {
        double range = 12 * distanceSensorLeft.getRangeInches();
        return range;
    }

    public double getRightDistanceInFeet() {
        double range = 12 * distanceSensorRight.getRangeInches();
        return range;
    }

    public void actuateIntake(boolean actuate) {
        left.set(actuate);
        right.set(actuate);
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
