package org.team114.lib.subsystem;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import org.team114.ocelot.RobotRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Structural class that runs subsystems.
 * @see Subsystem
 */
public class SubsystemManager {
    private final List<Subsystem> subsystems;
    private final Notifier notifier = new Notifier(this::step);
    private final RobotRegistry robotRegistry;

    /**
     * Creates a new manager from a list of subsystems.
     * @param subsystems a {@code List} of all subsystems
     */
    public SubsystemManager(RobotRegistry robotRegistry, List<? extends Subsystem> subsystems) {
        this.robotRegistry = robotRegistry;
        this.subsystems = new ArrayList<>(subsystems);
    }

    /**
     * Returns uptime in seconds.
     */
    private double timestamp() {
        return Timer.getFPGATimestamp();
    }

    private void step() {
        subsystems.forEach(system -> system.onStep(timestamp()));
    }

    /**
     * Starts up subsystems.&nbsp;Must not be called more than once before
     * {@link #stop()} is called.
     *
     * <p>This function tells a notifier to trigger every
     * "stepPeriod" (config file) seconds, calling the step method of each
     * subsystem.</p>

     */
    public void start() {
        subsystems.forEach(subsystem -> subsystem.onStart(timestamp()));
        notifier.startPeriodic(this.robotRegistry.getConfiguration().getDouble("stepPeriod"));
    }

    /**
     * Stops running the subsystems.
     */
    public void stop() {
        notifier.stop();
        subsystems.forEach(subsystem -> subsystem.onStop(timestamp()));
    }
}
