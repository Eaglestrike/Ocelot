package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Carriage {
    enum LiftStage {
        RAISED, STAGE_ONE, STAGE_TWO
    }

    private final Solenoid intake;
    private final Solenoid liftStageOne;
    private final Solenoid liftStageTwo;
    private final TalonSRX leftSpinner;
    private final TalonSRX rightSpinner;
    private final Ultrasonic distanceSensor;

    public Carriage(Solenoid intake,
                    Solenoid liftStageOne,
                    Solenoid liftStageTwo,
                    TalonSRX leftSpinner,
                    TalonSRX rightSpinner,
                    Ultrasonic distanceSensor) {
        this.intake = intake;
        this.liftStageOne = liftStageOne;
        this.liftStageTwo = liftStageTwo;
        this.leftSpinner = leftSpinner;
        this.rightSpinner = rightSpinner;
        this.distanceSensor = distanceSensor;
    }

    public void actuateIntake(boolean actuate) {
        intake.set(actuate);
    }

    public void actuageLift(LiftStage stage) {
        switch (stage) {
            case RAISED:
                liftStageOne.set(false);
                liftStageTwo.set(false);
            case STAGE_ONE:
                liftStageOne.set(true);
                liftStageTwo.set(false);
            case STAGE_TWO:
                liftStageOne.set(true);
                liftStageTwo.set(true);
        }
    }

    public void setSpin(boolean spin) {
        double velocity = spin ? 1 : 0;
        leftSpinner.set(ControlMode.Velocity, velocity);
        rightSpinner.set(ControlMode.Velocity, velocity);
    }

    public double getDistanceInFeet() {
        double range = toFeet(distanceSensor.getRangeInches());
        return range;
    }

    private static double toFeet(double rangeInches) {
        return 12.0 * rangeInches;
    }
}
