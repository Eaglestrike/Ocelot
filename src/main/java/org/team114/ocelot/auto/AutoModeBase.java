package org.team114.ocelot.auto;

import org.team114.lib.auto.actions.Action;

public abstract class AutoModeBase {
    protected double m_update_rate = 1.0 / 50.0;
    protected boolean m_active = false;

    protected abstract void routine();

    public void run() {
        m_active = true;
        routine();

        done();
        System.out.println("Auto mode done");
    }

    public void done() {
    }

    public void stop() {
        m_active = false;
    }

    public boolean isActive() {
        return m_active;
    }

    public void runAction(Action action) {
        action.start();

        while (isActive() && !action.finished()) {
            action.step();
            long waitTime = (long) (m_update_rate * 1000.0);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        action.stop();
    }
}