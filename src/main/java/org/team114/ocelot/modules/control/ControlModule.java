package org.team114.ocelot.modules.control;

import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.dagger.Left;
import org.team114.ocelot.dagger.Primary;
import org.team114.ocelot.dagger.Right;

import javax.inject.Singleton;

import static org.team114.ocelot.settings.RobotSettings.JOYSTICK_LEFT;
import static org.team114.ocelot.settings.RobotSettings.JOYSTICK_PRIMARY;
import static org.team114.ocelot.settings.RobotSettings.JOYSTICK_RIGHT;

@Module
public class ControlModule {
    @Provides
    @Singleton
    public static Controller provideController(DualController controller) {
        return controller;
    }

    @Provides
    @Left
    static Joystick provideLeftJoystick() {
        return new Joystick(JOYSTICK_LEFT);
    }

    @Provides
    @Right
    static Joystick provideRightJoystick() {
        return new Joystick(JOYSTICK_RIGHT);
    }

    @Provides
    @Primary
    static Joystick providePrimaryJoystick() {
        return new Joystick(JOYSTICK_PRIMARY);
    }

}
