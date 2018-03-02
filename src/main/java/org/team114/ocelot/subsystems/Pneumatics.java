package org.team114.ocelot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import org.team114.ocelot.modules.PneumaticPressureSensor;
import org.team114.ocelot.settings.Settings;

public class Pneumatics implements PneumaticsInterface {
    private final Compressor compressor;
    private final PneumaticPressureSensor pressureSensor;
    private double activationPressure = -1;
    private double pressureMargin = Settings.Pneumatics.DEFAULT_PRESSURE_MARGIN;

    public Pneumatics(Compressor compressor, PneumaticPressureSensor pressureSensor) {
        this.compressor = compressor;
        this.pressureSensor = pressureSensor;
    }

    @Override
    public void onStart(double timestamp) {}

    @Override
    public void onStop(double timestamp) {}

    @Override
    public void onStep(double timestamp) {
        // give control to compressor look if desired
        if (activationPressure < 0) {
            compressor.start();
            return;
        }

        // handle thresholding
        if (getPressure() < activationPressure && !compressor.getClosedLoopControl()) {
            compressor.start();
            return;
        }

        if (getPressure() > (activationPressure + pressureMargin) && compressor.getClosedLoopControl()) {
            compressor.stop();
            return;
        }
    }

    @Override
    public double getPressure() {
        return pressureSensor.getPressure();
    }

    @Override
    public boolean compressing() {
        return compressor.enabled();
    }

    @Override
    public void setMinimumPressure(double pressure) {
        activationPressure = Math.min(pressure, 100);
    }

    @Override
    public void unset() {
        setMinimumPressure(-1);
    }

    @Override
    public void setPressureMargin(double margin) {
        pressureMargin = margin;
    }
}
