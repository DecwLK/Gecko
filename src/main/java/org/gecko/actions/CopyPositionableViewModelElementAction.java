package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class CopyPositionableViewModelElementAction extends Action {
    CopyPositionableViewModelElementAction(
            ActionFactory actionFactory,
            EditorViewModel editorViewModel,
            List<PositionableViewModelElement<?>> positionableViewModelElement) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
