package org.gecko.actions;

import org.gecko.viewmodel.BlockViewModelElement;

public class ScaleBlockViewModelElementAction extends Action {
    ScaleBlockViewModelElementAction(
            BlockViewModelElement<?> element, double scaleFactor) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
