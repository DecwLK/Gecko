package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

import java.util.List;

public class SelectAction extends Action {

    SelectAction(
            ActionFactory actionFactory,
            EditorViewModel editorViewModel,
            PositionableViewModelElement<?> element,
            boolean newSelection) {}

    SelectAction(
            ActionFactory actionFactory,
            EditorViewModel editorViewModel,
            List<PositionableViewModelElement<?>> elements,
            boolean newSelection) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
