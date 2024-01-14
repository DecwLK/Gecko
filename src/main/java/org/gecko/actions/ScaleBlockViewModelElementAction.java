package org.gecko.actions;

import org.gecko.viewmodel.SelectionManager;

public class ScaleBlockViewModelElementAction extends Action {
    ScaleBlockViewModelElementAction(
            SelectionManager selectionManager, double scaleFactor) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
