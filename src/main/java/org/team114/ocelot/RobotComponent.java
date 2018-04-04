package org.team114.ocelot;

import dagger.Component;
import org.team114.ocelot.auto.AutoModeSelector;
import org.team114.ocelot.controllers.Controller;
import org.team114.ocelot.controllers.ControllerModule;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.subsystems.Pneumatics;
import org.team114.ocelot.subsystems.SubsystemModule;
import org.team114.ocelot.subsystems.Superstructure;

import javax.inject.Singleton;

@Component(modules = {SubsystemModule.class, ControllerModule.class})
@Singleton
public interface RobotComponent {
    Drive drive();
    Pneumatics pneumatics();
    Superstructure superstructure();
    Controller controller();
    AutoModeSelector autoModeSelector();
    TeleopInputDelegator teleopInputDelegator();
}
