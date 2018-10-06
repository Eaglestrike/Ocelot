package org.team114.ocelot.subsystems;

import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.util.DriveSignal;
import org.team114.ocelot.util.motion.PurePursuitController;

public interface Drive extends Subsystem {
    void zeroAllSensors();
    
    void setGear(State state);

    State getGear();

    void setDriveSignal(DriveSignal signal);

    void setDriveArcCommand(PurePursuitController.DriveArcCommand arc);

    void prepareForAuto();

    void prepareForTeleop();

    enum State {
        HIGH, LOW
    }
}
