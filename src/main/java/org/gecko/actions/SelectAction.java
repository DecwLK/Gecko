package org.gecko.actions;

import java.util.ArrayList;
import java.util.List;
import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.PositionableViewModelElement;
import org.gecko.viewmodel.SelectionManager;

public class SelectAction extends Action {
    private final SelectionManager selectionManager;
    private final List<PositionableViewModelElement<?>> elementsToSelect;
    private final boolean newSelection;

    SelectAction(EditorViewModel editorViewModel, List<PositionableViewModelElement<?>> elements, boolean newSelection) {
        this.selectionManager = editorViewModel.getSelectionManager();
        this.elementsToSelect = new ArrayList<>(elements);
        this.newSelection = newSelection;
    }

    @Override
    void run() {
        if (newSelection) {
            selectionManager.deselectAll();
        }

        selectionManager.select(elementsToSelect);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
