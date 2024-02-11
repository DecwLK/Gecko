package org.gecko.actions;

import org.gecko.exceptions.GeckoException;

/** An abstract representation of an operation that can be executed in the Gecko Graphic Editor. An {@link Action} can be run or undone. The provided methods must be implemented by concrete actions. */
public abstract class Action {
    /**
     * Runs the action.
     *
     * @return True if the action was successful, false if an error was detected before modifications were made.
     * @throws GeckoException If an error was detected after modifications were already made.
     */
    abstract boolean run() throws GeckoException;

    /**
     * Gets the undo action for this action.
     *
     * @return The undo action for this action if it is undoable, null else.
     */
    abstract Action getUndoAction(ActionFactory actionFactory);
}
