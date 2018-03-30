package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.auto.modes.CrossBaseLine;
import org.team114.ocelot.auto.modes.MiddleToSwitchCube;
import org.team114.ocelot.auto.modes.TestArcMode;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Superstructure;

import java.util.function.Supplier;

public class AutoModeSelector {
    private final SendableChooser<Supplier<AutoModeBase>> chooser = new SendableChooser();

    public AutoModeSelector(Drive drive, Superstructure sstruct, RobotState rstate) {
        chooser.addDefault("Baseline", () -> new CrossBaseLine(drive, sstruct));
        chooser.addObject("Middle to Switch Sides", () -> new MiddleToSwitchCube(drive, sstruct, rstate));
        chooser.addObject("Test Arc", () -> new TestArcMode(drive, sstruct));
        SmartDashboard.putData("Auto Mode Selector", chooser);
    }

    public AutoModeBase getAutoMode() {
        return chooser.getSelected().get();
    }
}
