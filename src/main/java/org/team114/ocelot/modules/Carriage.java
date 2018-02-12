package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;

public class Carriage {
    public enum LiftStage {
        RAISED, STAGE_ONE, STAGE_TWO
    }

    private final Solenoid intake;
    private final Solenoid liftStageOne;
    private final Solenoid liftStageTwo;
    private final TalonSRX leftSpinner;
    private final TalonSRX rightSpinner;
    private final DistanceSensor distanceSensor;

    public Carriage(Solenoid intake,
                    Solenoid liftStageOne,
                    Solenoid liftStageTwo,
                    TalonSRX leftSpinner,
                    TalonSRX rightSpinner,
                    DistanceSensor distanceSensor) {
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

    public void actuateLift(LiftStage stage) {
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

    /**
     * @return distance the carriage's sensor reads, in feet.
     */
    public double getDistanceToBox() {
        return distanceSensor.get();
    }
}
