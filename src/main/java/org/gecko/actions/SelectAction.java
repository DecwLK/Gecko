package org.gecko.actions;

import java.util.HashSet;
import java.util.Set;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SelectionManager;

public class SelectAction extends Action {
    private final SelectionManager selectionManager;
    private final Set<PositionableViewModelElement<?>> elementsToSelect;
    private final boolean newSelection;

    SelectAction(EditorViewModel editorViewModel, Set<PositionableViewModelElement<?>> elements, boolean newSelection) {
        this.selectionManager = editorViewModel.getSelectionManager();
        this.elementsToSelect = new HashSet<>(elements);
        this.newSelection = newSelection;
    }

    @Override
    void run() {
        if (!newSelection) {
            elementsToSelect.addAll(selectionManager.getCurrentSelection());
        }
        selectionManager.select(elementsToSelect);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
