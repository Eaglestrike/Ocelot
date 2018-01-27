package org.team114.lib.auto.actions;

import java.util.List;

/**
 * An action that executes several actions in parallel, each running in its own thread.
 */
public class ParallelAction extends CompositeAction {
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
    public ParallelAction(List<Action> actions) {
        super(actions);
    }

    @Override
    public boolean finished() {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);

            // if any action is not finished - we are not finished
            if (!action.finished()) {
                return false;
            }

            // when an action is finished it is removed from the list of running actions
            action.stop();
            actions.remove(i);
            i -= 1;
        }

        return true;
    }

    @Override
    public void start() {
        for (Action action : actions) {
            action.start();
        }
    }

    @Override
    public void stop() {
        for (Action action : actions) {
            action.stop();
        }
    }

    @Override
    public void step() {
        for (Action action : actions) {
            action.step();
        }
    }
}
