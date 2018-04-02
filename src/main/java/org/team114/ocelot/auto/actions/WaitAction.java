package org.team114.ocelot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;

public class WaitAction implements Action {
    private double initial;
    private double seconds;

    public WaitAction(double seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean finished() {
        return (Timer.getFPGATimestamp() - initial > seconds);
    }

    @Override
    public void start() {
        initial = Timer.getFPGATimestamp();
    }

    @Override
    public void stop() {
    }

    @Override
    public void step() {
    }
}
