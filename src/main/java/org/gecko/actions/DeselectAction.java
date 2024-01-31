package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SelectionManager;

public class DeselectAction extends Action {
    private final SelectionManager selectionManager;

    DeselectAction(EditorViewModel editorViewModel) {
        this.selectionManager = editorViewModel.getSelectionManager();
    }

    @Override
    void run() {
        selectionManager.deselectAll();
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
