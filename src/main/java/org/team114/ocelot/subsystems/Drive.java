package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.modules.GearShifter;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PurePursuitController;

public interface Drive extends Subsystem {
    void setGear(GearShifter.State state);

    void setDriveSignal(DriveSignal signal);

    void setDriveArcCommand(PurePursuitController.DriveArcCommand arc);

    void prepareForAuto();

    void prepareForTeleop();
}
