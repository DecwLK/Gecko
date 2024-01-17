package org.gecko.actions;

import org.gecko.viewmodel.SelectionManager;

public class SelectionHistoryBackAction extends Action {
    private final SelectionManager selectionManager;

    SelectionHistoryBackAction(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    @Override
    void run() {
        this.selectionManager.goBack();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
