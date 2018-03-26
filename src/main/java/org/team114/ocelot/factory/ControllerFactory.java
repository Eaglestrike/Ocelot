package org.team114.ocelot.factory;

import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.controllers.Controller;
import org.team114.ocelot.controllers.StickDriveOiPanel;
import org.team114.ocelot.controllers.XboxController;

public final class ControllerFactory {

    private ControllerFactory() {
        throw new AssertionError("Constructor must not be called on utility class.");
    }

    public static Controller stickDriveOiPanel () {

        return new StickDriveOiPanel(
                new Joystick(0),
                new Joystick(1),
                new Joystick(2));
    }
}
