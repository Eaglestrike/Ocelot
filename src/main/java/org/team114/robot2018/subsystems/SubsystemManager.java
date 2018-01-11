package org.team114.robot2018.subsystems;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

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

    private final List<Subsystem> subsystems;
    private final Notifier notifier = new Notifier(this::step);

    /**
     * Creates a new manager from a list of subsystems.
     * @param subsystems a {@code List} of all subsystems
     */
    public SubsystemManager(List<Subsystem> subsystems) {
        this.subsystems = subsystems;
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

    private void step() {
        subsystems.forEach(system -> system.onStep(timestamp()));
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
