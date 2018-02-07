package org.team114.lib.subsystem;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import org.team114.lib.util.DashboardHandle;

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

    private final List<Subsystem> subsystems;
    private final Notifier notifier = new Notifier(this::step);
    private final DashboardHandle hertzDB = new DashboardHandle("Subsystem Hz");

    private double lastTimeStamp;

    /**
     * Creates a new manager from a list of subsystems.
     * @param subsystems a {@code List} of all subsystems
     */
    public SubsystemManager(List<? extends Subsystem> subsystems) {
        this.subsystems = new ArrayList<>(subsystems);
    }

    /**
     * Creates a new manager with subsystems, using a variadic constructor
     * @param subsystems all the subsystems
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
        double current = Timer.getFPGATimestamp();
        hertzDB.put(1/(current - lastTimeStamp));
        lastTimeStamp = current;
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
        notifier.startPeriodic(STEP_PERIOD);
        lastTimeStamp = Timer.getFPGATimestamp();
    }

    /**
     * Stops running the subsystems.
     */
    public void stop() {
        notifier.stop();
        subsystems.forEach(subsystem -> subsystem.onStop(timestamp()));
    }
}
