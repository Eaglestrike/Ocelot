package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PurePursuitController;

public interface Drive extends Subsystem {
    void setGear(State state);

    void setDriveSignal(DriveSignal signal);

    void setDriveArcCommand(PurePursuitController.DriveArcCommand arc);

    void prepareForAuto();

    void prepareForTeleop();

    enum State {
        HIGH, LOW
    }
}
