/**
 *<p>Actions represent something that the robot can do. Some actions will tend to be executed for the
 * entire auton (modes). Others are intended to be used in combination with others to form a mode. There is at
 * present no code distinction between the two types of actions, so in theory any single action can be executed as the
 * entire mode. In fact, actions are general enough that they can be applied outside of autonomous, perhaps
 * for a self-test system.</p>
 *
 * <p>An action is just a runnable. This means that an action's operation is controlled by the
 * action runner, not by the action itself. The caller of an action can decide to run it synchronously or
 * in a new thread, and can choose to schedule the action's execution at its pleasure. Although this provides less
 * flexibility than some alternate layouts (e.g. one using steps), it allows for actions to be written in a
 * simple way.</p>
 */
package org.team114.lib.auto.actions;
