package org.team114.ocelot.subsystems.superstructure;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import org.team114.ocelot.settings.Settings;

import javax.inject.Singleton;

/**
 * The grabber/thrower unit of the robot.
 */
@Singleton
public class Carriage {

    private final Solenoid intake;
    private final Solenoid liftStageOne;
    private final Solenoid liftStageTwo;
    private final TalonSRX leftSpinner;
    private final TalonSRX rightSpinner;
    private final ProximitySensor distanceSensor;
    private CarriageElevationStage currentStage = CarriageElevationStage.RAISED;
    private boolean intakePneumatic = false;

    Carriage(Solenoid intake,
             Solenoid liftStageOne,
             Solenoid liftStageTwo,
             TalonSRX leftSpinner,
             TalonSRX rightSpinner,
             ProximitySensor distanceSensor) {
        this.intake = intake;
        this.liftStageOne = liftStageOne;
        this.liftStageTwo = liftStageTwo;
        this.leftSpinner = leftSpinner;
        this.rightSpinner = rightSpinner;

        this.leftSpinner.setNeutralMode(NeutralMode.Coast);
        this.rightSpinner.setNeutralMode(NeutralMode.Coast);

        this.distanceSensor = distanceSensor;
    }

    void actuateIntake(boolean actuate) {
        if (intakePneumatic == actuate) {
            return;
        }
        intakePneumatic = actuate;
        intake.set(actuate);
    }

    void actuateLift(CarriageElevationStage stage) {
        if (stage == currentStage) {
            return;
        }

        currentStage = stage;
        switch (currentStage) {
            case RAISED:
                liftStageOne.set(false);
                liftStageTwo.set(false);
                break;
            case MIDDLE:
                liftStageOne.set(false);
                liftStageTwo.set(true);
                break;
            case LOWERED:
                liftStageOne.set(true);
                liftStageTwo.set(true);
                break;
        }
    }

    void setSpin(double command) {
        leftSpinner.set(ControlMode.PercentOutput, -command*0.75);
        rightSpinner.set(ControlMode.PercentOutput, command);
    }

    public double getDistance() {
        return distanceSensor.get();
    }

    void setSpeedToProximitySensor() {
//        if (distanceSensor.get() < Settings.Carriage.BOX_DISTANCE_INTAKE_THRESHOLD_CM) {
//            setSpin(Settings.Carriage.INTAKE_IN_LOW_VOLTAGE_COMMAND);
//        } else {
//            setSpin(0);
//        }
        setSpin(Settings.Carriage.INTAKE_IN_LOW_VOLTAGE_COMMAND);
    }
}
