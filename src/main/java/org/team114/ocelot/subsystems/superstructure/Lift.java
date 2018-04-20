package org.team114.ocelot.subsystems.superstructure;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.settings.Settings;

import javax.inject.Singleton;

/**
 * The elevator for the robot.
 */
@Singleton
class Lift {
    private final TalonSRX masterTalon;
    private final TalonSRX slaveTalon;

    Lift(TalonSRX masterTalon, TalonSRX slaveTalon) {
        this.masterTalon = masterTalon;

        this.slaveTalon = slaveTalon;
        this.slaveTalon.set(ControlMode.Follower, masterTalon.getDeviceID());
        
        // limit switch
        this.masterTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyOpen, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
                LimitSwitchNormal.NormallyOpen, Settings.TALON_CONFIG_TIMEOUT_MS);

        // sensors and directions
        this.masterTalon.configSelectedFeedbackSensor(
                FeedbackDevice.CTRE_MagEncoder_Relative,
                Settings.Lift.MAGIC_PID_LOOP_INDEX,
                Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.setSensorPhase(true);

        this.masterTalon.setInverted(false);
        this.slaveTalon.setInverted(true);

        // neutral modes
        this.masterTalon.setNeutralMode(NeutralMode.Brake);
        this.slaveTalon.setNeutralMode(NeutralMode.Brake);

		// Set relevant frame periods to be at least as fast as periodic rate
        this.masterTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Settings.TALON_CONFIG_TIMEOUT_MS);

		// set the peak and nominal outputs
        this.masterTalon.configNominalOutputForward(0, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configNominalOutputReverse(0, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configPeakOutputForward(1, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configPeakOutputReverse(-1, Settings.TALON_CONFIG_TIMEOUT_MS);

		// set closed loop gains in slot0 - see documentation
        this.masterTalon.selectProfileSlot(Settings.Lift.MAGIC_PID_SLOT_INDEX, Settings.Lift.MAGIC_PID_LOOP_INDEX);
        this.masterTalon.config_kF(Settings.Lift.MAGIC_PID_SLOT_INDEX, 0.730714286, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.config_kP(Settings.Lift.MAGIC_PID_SLOT_INDEX, 4.8, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.config_kI(Settings.Lift.MAGIC_PID_SLOT_INDEX, 0.05, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.config_IntegralZone(Settings.Lift.MAGIC_PID_SLOT_INDEX, 35, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.config_kD(Settings.Lift.MAGIC_PID_SLOT_INDEX, 18, Settings.TALON_CONFIG_TIMEOUT_MS);
		/* set acceleration and vcruise velocity - see documentation */
        this.masterTalon.configMotionCruiseVelocity(1300, Settings.TALON_CONFIG_TIMEOUT_MS);
        this.masterTalon.configMotionAcceleration(1800, Settings.TALON_CONFIG_TIMEOUT_MS);
		/* zero the sensor */
    }

    boolean zeroLowerIfNecessary() {
        if (masterTalon.getSensorCollection().isRevLimitSwitchClosed()) {
            //TODO is it really zero?
            masterTalon.setSelectedSensorPosition(0, Settings.Lift.MAGIC_PID_LOOP_INDEX, 0);
            return true;
        }
        return false;
    }

    public boolean zeroUpperIfNecessary() {
        if (masterTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
            masterTalon.setSelectedSensorPosition(Settings.Lift.MAX_HEIGHT_TICKS, 0, 0);
            return true;
        }
        return false;
    }

    /**
     * Sets the setpoint of the lift.
     * @param height measured in ticks
     */
    void goToHeight(int height) {
        masterTalon.set(ControlMode.MotionMagic, height);
        SmartDashboard.putNumber("master current", this.masterTalon.getOutputCurrent());
        SmartDashboard.putNumber("slave current", this.slaveTalon.getOutputCurrent());
//        System.out.println("M, S:: "+ this.masterTalon.getOutputCurrent() + "    " + this.slaveTalon.getOutputCurrent());
    }

    //TODO add p controller to this mode
    void manualControl(double command) {
        masterTalon.set(ControlMode.PercentOutput, command);
//        slaveTalon.set(ControlMode.PercentOutput, command);
//        System.out.println(command);
    }

    //get the height in feet
    int getHeight() {
        return masterTalon.getSelectedSensorPosition(Settings.Lift.MAGIC_PID_LOOP_INDEX);
    }

    public boolean upperLimitSwitch() {
        return masterTalon.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean lowerLimitSwitch() {
        return masterTalon.getSensorCollection().isRevLimitSwitchClosed();
    }

    private static double convertTicksToFeet(int ticks) {
        double revolutions = (double)ticks / (double)Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        double feet = revolutions * Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        return feet;
    }

    private static int convertFeetToTicks(double feet) {
        double revolutions = feet / Settings.Lift.CLIMBER_FEET_PER_REVOLUTION;
        double ticks = revolutions * (double)Settings.Lift.ENCODER_TICKS_PER_REVOLUTION;
        return (int)ticks;
    }
}
