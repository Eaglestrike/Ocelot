package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.ocelot.Registry;
import org.team114.ocelot.subsystems.AbstractDrive;
import org.team114.ocelot.util.DriveSignal;

public class DriveCommandAction implements Action {
    private Registry registry;
    private final DriveSignal signal;

    public DriveCommandAction(Registry registry, DriveSignal signal) {
        this.registry = registry;
        this.signal = signal;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void start() {
        registry.get(AbstractDrive.class).setDriveSignal(signal);
    }

    @Override
    public void stop() {
        // for testing /validation
        registry.get(AbstractDrive.class).setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        registry.get(AbstractDrive.class).setDriveSignal(signal);
    }
}
