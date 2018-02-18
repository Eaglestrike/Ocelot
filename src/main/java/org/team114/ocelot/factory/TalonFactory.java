package org.team114.ocelot.factory;

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
        return new TalonSRX(id);
    }
}
