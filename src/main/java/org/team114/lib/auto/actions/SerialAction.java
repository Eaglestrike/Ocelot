package org.team114.lib.auto.actions;

import java.util.List;

/**
 * An action that runs actions in sequence. This is useful if several actions need to be run where one only
 * one is possible (e.g. to set a sequence of actions in a {@link ParallelAction}). It should be used sparingly, and
 * a new action should be preferred if the sequence is likely to be reused.
 */
public class SerialAction extends CompositeAction {

    /**
     * Create a new instance of this class from actions given as parameters, using a variadic constructor.
     * @param actions the actions to be executed
     */
    public SerialAction(Runnable ...actions) {
        super(actions);
    }

    /**
     * Create a new instance of this class from a list of actions.
     * @param actions a list of the action to be executed
     */
    public SerialAction(List<? extends Runnable> actions) {
        super(actions);
    }

    /**
     * Run each action in the list provided upon construction sequentially.
     */
    @Override
    public void run() {
        for (Runnable action: actions) {
             action.run();
        }
    }
}
