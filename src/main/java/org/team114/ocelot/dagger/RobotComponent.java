package org.team114.ocelot.dagger;

import dagger.Component;
import org.team114.ocelot.RobotState;
import org.team114.ocelot.modules.Controller;
import org.team114.ocelot.modules.Gyro;
import org.team114.ocelot.subsystems.AbstractDrive;

import javax.inject.Singleton;

@Component(modules = RobotModule.class)
@Singleton
public interface RobotComponent {
    AbstractDrive drive();
    Gyro gyro();
    RobotState robotState();
    Controller controller();

}
