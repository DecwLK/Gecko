package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.StateViewModel;

public class SetStartStateViewModelElementAction extends Action {

    SetStartStateViewModelElementAction(
            ActionFactory actionFactory,
            StateViewModel stateViewModel,
            EditorViewModel editorViewModel) {}

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
