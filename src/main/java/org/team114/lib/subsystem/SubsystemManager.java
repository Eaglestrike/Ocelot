package org.team114.lib.subsystem;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Structural class that runs subsystems.
 * @see Subsystem
 */
public class SubsystemManager {
    /**
     * Time between subsystem steps in seconds (currently {@value}).
     */
    public static final double STEP_PERIOD = 0.005; //200 times a second

    protected final List<Subsystem> subsystems;
    protected final Notifier notifier = new Notifier(this::step);

    /**
     * Creates a new manager from a list of subsystems.
     * @param subsystems a {@code List} of all subsystems
     */
    public SubsystemManager(List<? extends Subsystem> subsystems) {
        this.subsystems = new ArrayList<>(subsystems);
    }

    /**
     * Creates a new manager with subsystems, using a variadic constructor.
     *
     * @param subsystems every subsystem
     */
    public SubsystemManager(Subsystem... subsystems) {
        this(Arrays.asList(subsystems));
    }

    /**
     * Returns uptime in seconds.
     */
    private double timestamp() {
        return Timer.getFPGATimestamp();
    }

    private void remove(Subsystem subsystem) {
        subsystem.onStop(timestamp());
        subsystems.remove(subsystem);
    }

    private void step() {
        boolean done = true;
        for(Subsystem system: subsystems) {
            system.onStep(timestamp());
            done &=system.finished();
        }
        if (done) {
            stop();
        }
    }

    /**
     * Starts up subsystems.&nbsp;Must not be called more than once before
     * {@link #stop()} is called.
     *
     * <p>This function tells a notifier to trigger every
     * {@link SubsystemManager#STEP_PERIOD} seconds, calling the step method of each
     * subsystem.</p>

     */
    public void start() {
        subsystems.forEach(subsystem -> subsystem.onStart(timestamp()));
        notifier.startPeriodic(SubsystemManager.STEP_PERIOD);
    }

    /**
     * Stops running the subsystems.
     */
    public void stop() {
        notifier.stop();
        subsystems.forEach(subsystem -> subsystem.onStop(timestamp()));
    }
}
