package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class CutPositionableViewModelElementAction extends Action {
    CutPositionableViewModelElementAction(
            ActionFactory actionFactory,
            EditorViewModel editorViewModel,
            List<PositionableViewModelElement<?>> elements) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
