package org.team114.ocelot.dagger;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.Joystick;
import org.team114.ocelot.modules.Controller;
import org.team114.ocelot.modules.DualController;
import org.team114.ocelot.modules.RobotSide;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.subsystems.Drive;

import javax.inject.Singleton;

import static org.team114.ocelot.settings.RobotSettings.*;

@Module
public class RobotModule {
    @Provides
    @Singleton
    public static AbstractDrive provideAbstractDrive(Drive drive) {
        return drive;
    }

    @Provides
    @Singleton
    public static Controller provideController(DualController controller) {
        return controller;
    }

    @Provides
    @Left
    @Singleton
    public static RobotSide provideLeftRobotSide() {
        return new RobotSide(new TalonSRX(TALON_LEFT_1), new TalonSRX(TALON_LEFT_2));
    }

    @Provides
    @Right
    @Singleton
    public static RobotSide provideRightRobotSide() {
        return new RobotSide(new TalonSRX(TALON_RIGHT_1), new TalonSRX(TALON_RIGHT_2));
    }

    @Provides
    @Left
    @Singleton
    public static Joystick provideLeftJoystick() {
        return new Joystick(JOYSTICK_LEFT);
    }

    @Provides
    @Right
    @Singleton
    public static Joystick provideRightJoystick() {
        return new Joystick(JOYSTICK_RIGHT);
    }

    @Provides
    @Primary
    @Singleton
    public static Joystick providePrimaryJoystick() {
        return new Joystick(JOYSTICK_PRIMARY);
    }

}
