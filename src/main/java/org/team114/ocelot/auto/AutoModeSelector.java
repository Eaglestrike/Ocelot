package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.auto.modes.CrossBaseLine;
import org.team114.ocelot.auto.modes.MiddleToSwitchCube;
import org.team114.ocelot.auto.modes.TestArcMode;
import org.team114.ocelot.auto.modes.TestMode;

import javax.inject.Inject;
import java.util.function.Supplier;

public class AutoModeSelector {
    private final SendableChooser<Supplier<AutoModeBase>> chooser = new SendableChooser<>();

    @Inject
    public AutoModeSelector(CrossBaseLine crossBaseLine, MiddleToSwitchCube middleToSwitchCube,
                            TestMode testMode) {
        chooser.addDefault("Baseline", () -> crossBaseLine);
        chooser.addObject("Middle to Switch Sides", () -> middleToSwitchCube);
        chooser.addObject("Test Mode", () -> testMode);
        SmartDashboard.putData("Auto Mode Selector", chooser);
    }

    public AutoModeBase getAutoMode() {
        return chooser.getSelected().get();
    }
}
