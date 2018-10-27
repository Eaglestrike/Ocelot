package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.auto.modes.*;

import javax.inject.Inject;
import java.util.function.Supplier;

public class AutoModeSelector {
    private final SendableChooser<Supplier<AutoModeBase>> chooser = new SendableChooser<>();

    @Inject
    public AutoModeSelector(CrossBaseLine crossBaseLine, MiddleToSwitch middleToSwitchCube,
                            TestMode testMode, RightSideToScaleMode rightToScale, LeftSideToScaleMode leftToScale, RightSideOnlyMode rightOnly) {
        chooser.addDefault("Baseline", () -> crossBaseLine);
        chooser.addObject("Middle to Switch", () -> middleToSwitchCube);
        chooser.addObject("Left Side to Scale", () -> leftToScale);
        chooser.addObject("Right Side to Scale", () -> rightToScale);
        chooser.addObject("Right Side Only", () -> rightOnly);
        chooser.addObject("Test Mode", () -> testMode);
        SmartDashboard.putData("Auto Mode Selector", chooser);
    }

    public AutoModeBase getAutoMode() {
        return chooser.getSelected().get();
    }
}
