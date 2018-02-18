package org.team114.ocelot.factory;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

import static org.team114.ocelot.settings.Settings.TALON_CONFIG_TIMEOUT_MS;

public final class TalonFactory {

    private TalonFactory() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    public static TalonSRX new775ProTalon(int id) {
        TalonSRX talon = new TalonSRX(id);
        talon.configPeakCurrentLimit(Settings.PEAK_CURRENT_LIMIT_AMPS_775PRO, TALON_CONFIG_TIMEOUT_MS);
        return talon;
    }

    public static TalonSRX newSimTalon(int id) {
        TalonSRX talon = new TalonSRX(id);

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
}
