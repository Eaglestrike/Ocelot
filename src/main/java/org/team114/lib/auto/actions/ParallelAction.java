package org.team114.lib.auto.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * An action that executes several actions in parallel, each running in its own thread.
 */
public class ParallelAction extends CompositeAction {
    /**
     * An executor that is responsible for running the actions. The thread pool is cached
     * to create threads as needed. Threads will not be destroyed for 60 seconds, longer
     * than the autonomous phase, and the same thread pool is used for all ParallelActions
     * so overhead will be minimal.
     */
    private static ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Create a new instance of this class from actions given as parameters, using a variadic constructor.
     * @param actions the actions to be executed
     */
    public ParallelAction(Action ...actions) {
        super(actions);
    }

    /**
     * Create a new instance of this class from a list of actions.
     * @param actions a list of the action to be executed
     */
    public ParallelAction(List<? extends Action> actions) {
        super(actions);
    }


    /**
     * Execute every action concurrently, each one running on a thread from the pool.
     */
    @Override
    public void run() {

        List<Future<Object>> futures = new ArrayList<>();

        for (Action action : actions) {
            futures.add(executor.submit(action, null));
        }

        for (Future<Object> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e ) {
                //TODO error logging
                System.out.println("Exception in parallel execution: " + e.getMessage());
            }
        }
    }
}
