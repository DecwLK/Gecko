package org.gecko.actions;

import org.gecko.viewmodel.Renamable;

public class RenameViewModelElementAction extends Action {

    RenameViewModelElementAction(
            ActionFactory actionFactory, Renamable renamable, String newName) {}

    @Override
    void run() {}

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
