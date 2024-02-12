package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;

/**
 * A concrete representation of an {@link Action} that selects and focuses on a {link PositionableViewModelElement}.
 */
public class FocusPositionableViewModelElementAction extends Action {

    private final EditorViewModel editorViewModel;
    private final PositionableViewModelElement<?> element;

    FocusPositionableViewModelElementAction(
        EditorViewModel editorViewModel, PositionableViewModelElement<?> positionableViewModelElement) {
        this.editorViewModel = editorViewModel;
        this.element = positionableViewModelElement;
    }

    @Override
    boolean run() throws GeckoException {
        editorViewModel.getSelectionManager().select(element);
        editorViewModel.moveToFocusedElement();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
