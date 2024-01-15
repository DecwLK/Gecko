package org.gecko.actions;

import java.util.List;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class SelectAction extends Action {

    SelectAction(EditorViewModel editorViewModel, PositionableViewModelElement<?> element, boolean newSelection) {
    }

    SelectAction(EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements, boolean newSelection) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
