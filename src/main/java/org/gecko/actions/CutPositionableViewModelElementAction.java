package org.gecko.actions;

import java.util.List;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class CutPositionableViewModelElementAction extends Action {
    CutPositionableViewModelElementAction(EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
