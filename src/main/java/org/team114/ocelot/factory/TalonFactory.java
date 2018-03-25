package org.team114.ocelot.factory;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

import static org.team114.ocelot.settings.Settings.TALON_CONFIG_TIMEOUT_MS;

/**
 * Constructs appropriately configured talons from ids.
 */
public final class TalonFactory {

    private TalonFactory() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    public static TalonSRX new775ProTalon(int id) {
        TalonSRX talon = constructFactoryResetTalon(id);
        commonConfig(talon);

        talon.configPeakCurrentLimit(Settings.Lift.CURRENT_LIMIT_THRESHOLD, TALON_CONFIG_TIMEOUT_MS);
        talon.configPeakCurrentDuration(Settings.Lift.CURRENT_LIMIT_DURATION_MS, TALON_CONFIG_TIMEOUT_MS);
        talon.configContinuousCurrentLimit(Settings.Lift.CURRENT_LIMIT, TALON_CONFIG_TIMEOUT_MS);
        talon.enableCurrentLimit(true);

        return talon;
    }

    public static TalonSRX newCimTalon(int id) {
        TalonSRX talon = constructFactoryResetTalon(id);
        commonConfig(talon);

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
                TALON_CONFIG_TIMEOUT_MS);
        talon.setSelectedSensorPosition(0, 0, TALON_CONFIG_TIMEOUT_MS);
        talon.setSensorPhase(false);
        talon.getSensorCollection().setQuadraturePosition(0, TALON_CONFIG_TIMEOUT_MS);

        talon.configPeakCurrentLimit(Settings.DriveSide.CURRENT_LIMIT_THRESHOLD, TALON_CONFIG_TIMEOUT_MS);
        talon.configPeakCurrentDuration(Settings.DriveSide.CURRENT_LIMIT_DURATION_MS, TALON_CONFIG_TIMEOUT_MS);
        talon.configContinuousCurrentLimit(Settings.DriveSide.CURRENT_LIMIT, TALON_CONFIG_TIMEOUT_MS);
        talon.enableCurrentLimit(true);

        return new TalonSRX(id);
    }

    private static void commonConfig(TalonSRX talon) {
        talon.setInverted(false);
        talon.setSensorPhase(false);
        talon.setNeutralMode(NeutralMode.Brake);
    }

    public static TalonSRX factoryReset(TalonSRX t) {
        // source: https://github.com/CrossTheRoadElec/Phoenix-Documentation#factory-default-values
        // these settings can also be set with the physical reset button on the talon
        t.configOpenloopRamp(0, 0);
        t.configClosedloopRamp(0, 0);
        t.configPeakOutputForward(+1, 0);
        t.configPeakOutputReverse(-1, 0);
        t.configNominalOutputForward(0, 0);
        t.configNominalOutputReverse(0, 0);
        t.configNeutralDeadband(0.04, 0);
        t.configVoltageCompSaturation(0, 0);
        t.configVoltageMeasurementFilter(32, 0);
        t.configSelectedFeedbackSensor(FeedbackDevice.None, 0, 0);
//        t.configSelectedFeedbackCoefficient(	1.0);
        t.configRemoteFeedbackFilter(0, RemoteSensorSource.Off,0, 0);
        t.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.None, 0);
        t.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.None, 0);
        t.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.None, 0);
        t.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.None, 0);
        t.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, 0);
        t.configVelocityMeasurementWindow(64, 0);
        t.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen,0);
        t.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.NormallyOpen,0);
        t.configForwardSoftLimitThreshold(0, 0);
        t.configReverseSoftLimitThreshold(0, 0);
        t.configForwardSoftLimitEnable(false, 0);
        t.configReverseSoftLimitEnable(false, 0);
        for (int slotIdx = 0; slotIdx <= 1; slotIdx++) {
            t.config_kP(slotIdx,0, 0);
            t.config_kI(slotIdx,0, 0);
            t.config_kD(slotIdx,0, 0);
            t.config_kF(slotIdx,0, 0);
            t.config_IntegralZone(slotIdx,0, 0);
            t.configAllowableClosedloopError(slotIdx,0, 0);
            t.configMaxIntegralAccumulator(slotIdx, 0, 0);
        }
//        t.configClosedLoopPeakOutput(	1.0, 0, 0);
//        t.configClosedLoopPeriod(	1 ms, 0);
//        t.configAuxPIDPolarity(	false, 0);
        t.configMotionCruiseVelocity(	0, 0);
        t.configMotionAcceleration(	0, 0);
        t.configMotionProfileTrajectoryPeriod(	0, 0);
        t.configSetCustomParam(	0, 0, 0);
        t.configPeakCurrentLimit(	0, 0);
//        The factor default of this setting causes massive issues with current limiting
//        so choose the sensible default of 0
//        t.configPeakCurrentDuration(	Invalid, see errata.);
        t.configPeakCurrentDuration(0, 0);
        t.configContinuousCurrentLimit(0, 0);
        return t;
    }

    public static TalonSRX constructFactoryResetTalon(int id) {
        return factoryReset(new TalonSRX(id));
    }
}
