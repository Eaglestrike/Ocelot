package org.team114.ocelot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.settings.Settings;

public class Carriage {
    public enum ElevationStage {
        RAISED, STAGE_ONE, STAGE_TWO
    }

    private final Solenoid intake;
    private final Solenoid liftStageOne;
    private final Solenoid liftStageTwo;
    private final TalonSRX leftSpinner;
    private final TalonSRX rightSpinner;
    private final DistanceSensor distanceSensor;
    private ElevationStage currentStage = ElevationStage.RAISED;

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
        if (intake.get() != actuate) {
            intake.set(actuate);
        }
    }

    public void actuateLift(ElevationStage stage) {
        if (stage != currentStage) {
            currentStage = stage;
            switch (currentStage) {
                case RAISED:
                    liftStageOne.set(false);
                    liftStageTwo.set(false);
                    break;
                case STAGE_ONE:
                    liftStageOne.set(true);
                    liftStageTwo.set(false);
                    break;
                case STAGE_TWO:
                    liftStageOne.set(true);
                    liftStageTwo.set(true);
                    break;
            }
        }
    }

    public void setSpin(double command) {
        leftSpinner.set(ControlMode.PercentOutput, command);
        rightSpinner.set(ControlMode.PercentOutput, command);
    }

    /**
     * Returns distance the carriage's sensor reads, in feet.
     */
    public double getDistanceToBox() {
        return distanceSensor.get();
    }
}
