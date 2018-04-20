package org.team114.ocelot.controllers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.Joystick;

@Module
public abstract class ControllerModule {
    @Provides
    static StickDriveOiPanel provideStickDriveOiModule() {
        return new StickDriveOiPanel(
                new Joystick(0),
                new Joystick(1),
                new Joystick(2));
    }

    @Binds
    abstract Controller provideController(StickDriveOiPanel controller);

}
