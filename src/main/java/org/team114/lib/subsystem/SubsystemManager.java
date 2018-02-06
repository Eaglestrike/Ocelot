package org.team114.lib.subsystem;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import org.team114.ocelot.RobotRegistry;
import org.team114.ocelot.util.DashboardHandle;

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
    private final DashboardHandle hertzDB = new DashboardHandle("Subsystem Hz");

    private double lastTimeStamp;

    /**
     * Creates a new manager from a list of subsystems.
     * @param subsystems a {@code List} of all subsystems
     */
    public SubsystemManager(RobotRegistry robotRegistry, List<? extends Subsystem> subsystems) {
        this.robotRegistry = robotRegistry;
        this.subsystems = new ArrayList<>(subsystems);
    }

    /**
     * Creates a new manager with subsystems, using a variadic constructor
     * @param subsystems all the subsystems
     */
    public SubsystemManager(RobotRegistry robotRegistry, Subsystem... subsystems) {
        this(robotRegistry, Arrays.asList(subsystems));
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
     * "stepPeriod" (config file) seconds, calling the step method of each
     * subsystem.</p>
     */
    public void start() {
        subsystems.forEach(subsystem -> subsystem.onStart(timestamp()));
        notifier.startPeriodic(robotRegistry.getConfiguration().getDouble("stepPeriod"));
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
