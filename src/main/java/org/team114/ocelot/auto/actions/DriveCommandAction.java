package org.team114.ocelot.auto.actions;

import org.team114.lib.auto.actions.Action;
import org.team114.lib.subsystem.Subsystem;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.Subsystems;
import org.team114.ocelot.subsystems.Drive;
import org.team114.ocelot.util.DriveSignal;

public class DriveCommandAction implements Action {
    private RobotRegistry robotRegistry;
    private final DriveSignal signal;

    public DriveCommandAction(RobotRegistry robotRegistry, DriveSignal signal) {
        this.robotRegistry = robotRegistry;
        this.signal = signal;
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void start() {
        ((Drive) robotRegistry.get(Drive.class)).setDriveSignal(signal);
    }

    @Override
    public void stop() {
        // for testing /validation
        ((Drive) robotRegistry.get(Drive.class)).setDriveSignal(new DriveSignal(0,0));
    }

    @Override
    public void step() {
        ((Drive) robotRegistry.get(Drive.class)).setDriveSignal(signal);
    }
}
