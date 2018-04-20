package org.team114.ocelot.subsystems;

import dagger.Binds;
import dagger.Module;
import org.team114.ocelot.subsystems.drive.DriveModule;
import org.team114.ocelot.subsystems.drive.StandardDrive;
import org.team114.ocelot.subsystems.pneumatics.PneumaticsModule;
import org.team114.ocelot.subsystems.pneumatics.StandardPneumatics;
import org.team114.ocelot.subsystems.superstructure.StandardSuperstructure;
import org.team114.ocelot.subsystems.superstructure.SuperstructureModule;

@Module(includes = {DriveModule.class, PneumaticsModule.class, SuperstructureModule.class})
public abstract class SubsystemModule {
    @Binds
    public abstract Drive provideDrive(StandardDrive drive);

    @Binds
    public abstract Pneumatics providePneumatics(StandardPneumatics pneumatics);

    @Binds
    public abstract Superstructure provideSuperstructure(StandardSuperstructure superstructure);
}
