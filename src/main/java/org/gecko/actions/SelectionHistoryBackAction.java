package org.gecko.actions;

import org.gecko.viewmodel.SelectionManager;

public class SelectionHistoryBackAction extends Action {

    SelectionHistoryBackAction(
            SelectionManager selectionManager) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
