package org.team114.ocelot.subsystems.drive;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team114.ocelot.dagger.Left;
import org.team114.ocelot.dagger.Right;
import org.team114.ocelot.settings.Settings;

import static org.team114.ocelot.factory.TalonFactory.newCimTalon;

@Module
public abstract class DriveModule {
    @Binds
    abstract Gyro provideGyro(NavXGyro gyro);

    @Provides
    @Left
    static DriveSide provideLeftDriveSide() {
        return new DriveSide(
                newCimTalon(Settings.DriveSide.LEFT_MASTER),
                newCimTalon(Settings.DriveSide.LEFT_SLAVE));
    }

    @Provides
    @Right
    static DriveSide provideRightDriveSide() {
        return new DriveSide(
                newCimTalon(Settings.DriveSide.RIGHT_MASTER),
                newCimTalon(Settings.DriveSide.RIGHT_SLAVE));
    }

    @Provides
    static GearShifter provideShifter() {
        return new GearShifter(new DoubleSolenoid(
                Settings.GearShifter.HIGH_GEAR,
                Settings.GearShifter.LOW_GEAR));
    }
}
