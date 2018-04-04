package org.team114.ocelot.subsystems.pneumatics;

import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import org.team114.ocelot.settings.Settings;

@Module
public abstract class PneumaticsModule {
    @Provides
    static PneumaticPressureSensor providePressureSensor() {
        return new PneumaticPressureSensor(
                new AnalogInput(Settings.Pneumatics.PNEUMATIC_PRESSURE_SENSOR_ID));
    }

    @Provides
    static Compressor provideCompressor() {
        return new Compressor();
    }
}
