package org.gecko.actions;

public abstract class Action {
    abstract void run();
    abstract Action getUndoAction(ActionFactory actionFactory);
}
