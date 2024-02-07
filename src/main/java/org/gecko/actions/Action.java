package org.gecko.actions;

import org.gecko.exceptions.GeckoException;

public abstract class Action {
    abstract boolean run() throws GeckoException;

    abstract Action getUndoAction(ActionFactory actionFactory);


}
