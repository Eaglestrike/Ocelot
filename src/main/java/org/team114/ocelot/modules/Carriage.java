package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

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

    public Carriage(Solenoid leftIntake,
                    Solenoid rightIntake,
                    Solenoid rightLift1,
                    Solenoid rightLift2,
                    Solenoid leftLift1,
                    Solenoid leftLift2,
                    Ultrasonic leftDistanceSensor,
                    Ultrasonic rightDistanceSensor,
                    TalonSRX leftTalon,
                    TalonSRX rightTalon) {
        this.leftIntake = leftIntake;
        this.rightIntake = rightIntake;
        this.rightLift1 = rightLift1;
        this.rightLift2 = rightLift2;
        this.leftLift1 = leftLift1;
        this.leftLift2 = leftLift2;
        this.leftDistanceSensor = leftDistanceSensor;
        this.rightDistanceSensor = rightDistanceSensor;
        this.leftTalon = leftTalon;
        this.rightTalon = rightTalon;
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
