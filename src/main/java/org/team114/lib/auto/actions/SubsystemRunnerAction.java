package org.team114.lib.auto.actions;

import org.team114.lib.subsystem.Subsystem;
import org.team114.lib.subsystem.SubsystemManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SubsystemRunnerAction implements Runnable {

    private static class AutonomousSubsystemManager extends SubsystemManager {
        public Lock lock = new ReentrantLock();

        public AutonomousSubsystemManager(List<? extends Subsystem> subsystems) {
            super(subsystems);
        }

        @Override
        public void start() {
            lock.lock();
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
            lock.unlock();
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
        try {
            manager.lock.tryLock(15, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            // ignore, it means thread got killed.
            // still print the error for debugging purposes
            exception.printStackTrace();
        }
    }
}
