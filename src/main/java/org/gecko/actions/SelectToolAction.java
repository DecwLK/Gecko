package org.gecko.actions;

import org.gecko.tools.Tool;
import org.gecko.viewmodel.EditorViewModel;

public class SelectToolAction extends Action {

    SelectToolAction(ActionFactory actionFactory, EditorViewModel editorViewModel, Tool tool) {
    }

    @Override
    void run() {

    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
