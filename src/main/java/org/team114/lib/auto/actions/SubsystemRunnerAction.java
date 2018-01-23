package org.team114.lib.auto.actions;

import org.team114.lib.subsystem.Subsystem;
import org.team114.lib.subsystem.SubsystemManager;

import java.util.Arrays;
import java.util.List;

public class SubsystemRunnerAction implements Action {

    private static class AutonomousSubsystemManager extends SubsystemManager {
        public Object lock = new Object();

        public AutonomousSubsystemManager(List<? extends Subsystem> subsystems) {
            super(subsystems);
        }

        @Override
        public void stop() {
            super.stop();
            lock.notify();
        }
    }

    private AutonomousSubsystemManager manager;

    public SubsystemRunnerAction(List<? extends Subsystem> subsystems) {
        this.manager = new AutonomousSubsystemManager(subsystems);
    }

    public SubsystemRunnerAction(Subsystem... subsystems) {
        this(Arrays.asList(subsystems));
    }

    @Override
    public void run() {
        manager.start();
        // 15 seconds in milliseconds (length of autonomous period)
        long timeout = 15 * 1000;
        try {
            manager.lock.wait(timeout);
        } catch (InterruptedException exception) {
            // ignore, it means thread got killed.
            // still print the error for debugging purposes
            exception.printStackTrace();
        }
    }
}
