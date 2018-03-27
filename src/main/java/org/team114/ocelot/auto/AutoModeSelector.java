package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.auto.modes.CrossBaseLine;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;

import java.util.function.Supplier;

public class AutoModeSelector {
    private final SendableChooser<Supplier<AutoModeBase>> chooser = new SendableChooser();
    private AutoModeBase autoMode;

    public AutoModeSelector(Drive drive, Superstructure sstruct) {
        chooser.addDefault("Baseline", () -> new CrossBaseLine(drive, sstruct));
        chooser.addObject("Baseline 2", () -> new CrossBaseLine(drive, sstruct));
        chooser.addObject("Baseline 3", () -> new CrossBaseLine(drive, sstruct));
        SmartDashboard.putData(chooser);
    }

    public AutoModeBase getAutoMode() {
        return chooser.getSelected().get();
    }
}
