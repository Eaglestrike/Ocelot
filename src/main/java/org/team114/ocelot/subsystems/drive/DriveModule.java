package org.team114.ocelot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import dagger.Module;
import dagger.Provides;
import org.team114.ocelot.dagger.Left;
import org.team114.ocelot.dagger.Right;

import javax.inject.Singleton;

import static org.team114.ocelot.settings.RobotSettings.*;

@Module
public class DriveModule {
    @Provides
    @Singleton
    static AbstractDrive provideAbstractDrive(Drive drive) {
        return drive;
    }

    @Provides
    @Left
    static RobotSide provideLeftRobotSide() {
        return new RobotSide(new TalonSRX(TALON_LEFT_1), new TalonSRX(TALON_LEFT_2));
    }

    @Provides
    @Right
    static RobotSide provideRightRobotSide() {
        return new RobotSide(new TalonSRX(TALON_RIGHT_1), new TalonSRX(TALON_RIGHT_2));
    }
}
