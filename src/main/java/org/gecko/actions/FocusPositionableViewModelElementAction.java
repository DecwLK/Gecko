package org.gecko.actions;

import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.EditorViewModel;

public class FocusPositionableViewModelElementAction extends Action {
    public FocusPositionableViewModelElementAction(
            EditorViewModel editorViewModel,
            PositionableViewModelElement<?> positionableViewModelElement) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
