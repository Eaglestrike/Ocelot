package org.team114.ocelot.factory;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.controllers.DualController;
import org.team114.ocelot.controllers.XboxController;

public final class ControllerFactory {

    private ControllerFactory() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    public static DualController dualController() {

        return new DualController(
                new Joystick(0),
                new Joystick(1),
                new XboxController(new Joystick(2)));
    }
}
