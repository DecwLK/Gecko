package org.gecko.actions;

import org.gecko.viewmodel.EditorViewModel;
import org.gecko.viewmodel.SystemViewModel;

public class ViewSwitchAction extends Action {
    ViewSwitchAction(SystemViewModel systemViewModel, EditorViewModel currentEditorViewModel, boolean isAutomaton) {
    }

    @Override
    void run() {
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
