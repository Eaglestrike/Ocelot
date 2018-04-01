package org.team114.ocelot.auto;

import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.auto.actions.Action;

public abstract class AutoModeBase {
    protected double updateRate = 1.0 / 50.0;
    protected boolean active;

    protected abstract void routine();

    public void run() {
        active = true;
        routine();

        done();
        System.out.println("Auto mode done");
    }

    public void done() {
    }

    public void stop() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void runAction(Action action) {
        action.start();

        while (isActive() && !action.finished()) {
            action.step();
            long waitTime = (long) (updateRate * 1000.0);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        action.stop();
    }

    public void runActionTimeout(Action action, double seconds) {
        double start = Timer.getFPGATimestamp();
        action.start();

        while (isActive() && !action.finished() && (Timer.getFPGATimestamp() - start < seconds)) {
            action.step();
            long waitTime = (long) (updateRate * 1000.0);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        action.stop();
    }
}