package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

public class FocusPositionableViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final PositionableViewModelElement<?> element;

    FocusPositionableViewModelElementAction(EditorViewModel editorViewModel, PositionableViewModelElement<?> positionableViewModelElement) {
        this.editorViewModel = editorViewModel;
        this.element = positionableViewModelElement;
    }

    @Override
    void run() {
        editorViewModel.getSelectionManager().select(element);
        editorViewModel.moveToFocusedElement();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
