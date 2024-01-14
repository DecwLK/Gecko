package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class CopyPositionableViewModelElementAction extends Action {
    CopyPositionableViewModelElementAction(
            EditorViewModel editorViewModel,
            List<PositionableViewModelElement<?>> positionableViewModelElement) {}

    @Override
    void run() {
        //TODO how does copy work?
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
