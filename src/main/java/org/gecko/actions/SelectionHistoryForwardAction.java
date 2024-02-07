package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.SelectionManager;

public class SelectionHistoryForwardAction extends Action {
    private final SelectionManager selectionManager;

    public SelectionHistoryForwardAction(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    @Override
    boolean run() throws GeckoException {
        this.selectionManager.goForward();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
