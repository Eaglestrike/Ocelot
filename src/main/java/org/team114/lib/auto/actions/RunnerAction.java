package org.team114.lib.auto.actions;

/**
 * Wraps a runnable, turning it into an action.&nbsp;This should only be used if it's absolutely needed, because
 * a new action class is more reusable. An example of a case when this action is appropriate is for on-the-fly testing,
 * where the flexibility of a runnable lambda allows for quick diagnosis.
 */
public class RunnerAction extends Action {

    private Runnable runnable;

    /**
     * Creates a wrapper from a runnable.
     * @param runnable the runnable to be wrapped
     */
    public RunnerAction(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Runs the runnable.
     */
    @Override
    public void run() {
        runnable.run();
    }
}
