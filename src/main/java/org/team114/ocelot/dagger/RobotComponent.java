package org.team114.ocelot.dagger;

import dagger.Component;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.control.ControlModule;
import org.team114.ocelot.modules.control.Controller;
import org.team114.ocelot.subsystems.drive.AbstractDrive;
import org.team114.ocelot.subsystems.drive.DriveModule;

import javax.inject.Singleton;

@Component(modules = {DriveModule.class, ControlModule.class})
@Singleton
public interface RobotComponent {
    AbstractDrive drive();
    RobotState robotState();
    Controller controller();

}
