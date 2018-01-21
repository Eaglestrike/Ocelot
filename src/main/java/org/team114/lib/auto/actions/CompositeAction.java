package org.team114.lib.auto.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents any action that is built from component actions. This action will probably execute at least some
 * of the component actions, but the manner in which it does so is implementation defined. Example use cases include
 * executing actions in a different order (e.g. {@link org.team114.lib.auto.actions.ParallelAction})
 * or some form of conditional execution.
 */
public abstract class CompositeAction extends Action {
    /**
     * A list of actions input when the class is constructed. The use of this list is defined
     * by the subclass, but some or all of the actions will ordinarily be executed.
     */
    protected List<Action> actions;

    /**
     * Create a new instance of this class from a list of actions.
     * @param actions a list of the action to be executed
     */
    public CompositeAction(List<? extends Action> actions) {

        this.actions = new ArrayList<>(actions);
    }

    /**
     * Create a new instance of this class from actions given as parameters, using a variadic constructor.
     * @param actions the actions to be executed
     */
    public CompositeAction(Action... actions) {
        this(Arrays.asList(actions));
    }
}
