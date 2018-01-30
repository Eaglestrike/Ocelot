package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.Subsystems;
import org.team114.ocelot.util.DriveSignal;

public class DriveCommandAction implements Action {
    private Subsystems subsystems;
    private final DriveSignal signal;

    public DriveCommandAction(Subsystems subsystems, DriveSignal signal) {
        this.subsystems = subsystems;
        this.signal = signal;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void start() {
        subsystems.getDrive().setDriveSignal(signal);
    }

    @Override
    public void stop() {
        subsystems.getDrive().setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        subsystems.getDrive().setDriveSignal(signal);
    }
}
