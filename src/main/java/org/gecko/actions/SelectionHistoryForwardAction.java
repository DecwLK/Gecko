package org.gecko.actions;

import org.gecko.viewmodel.SelectionManager;

public class SelectionHistoryForwardAction extends Action {
    private final SelectionManager selectionManager;

    public SelectionHistoryForwardAction(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    @Override
    void run() {
        this.selectionManager.goForward();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
