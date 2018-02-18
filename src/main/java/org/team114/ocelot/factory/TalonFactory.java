package org.team114.ocelot.factory;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team114.ocelot.settings.Settings;

public final class TalonFactory {

    private TalonFactory() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    public static TalonSRX new775ProTalon(int id) {
        TalonSRX talon = new TalonSRX(id);
        talon.configPeakCurrentLimit(Settings.PEAK_CURRENT_LIMIT_AMPS_775PRO,
                Settings.STANDARD_TALON_CONFIG_TIMEOUT_MS);
        return talon;
    }

    public static TalonSRX newSimTalon(int id) {
        TalonSRX talon = new TalonSRX(id);

        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        talon.setSelectedSensorPosition(0, 0, 0);
        talon.setSensorPhase(false);
        talon.getSensorCollection().setQuadraturePosition(0, 0);

        talon.configPeakCurrentLimit(Settings.DriveSide.CURRENT_LIMIT_THRESHOLD, 0);
        talon.configPeakCurrentDuration(Settings.DriveSide.CURRENT_LIMIT_DURATION_MS, 0);
        talon.configContinuousCurrentLimit(Settings.DriveSide.CURRENT_LIMIT, 0);
        talon.enableCurrentLimit(true);

        return new TalonSRX(id);
    }
}
