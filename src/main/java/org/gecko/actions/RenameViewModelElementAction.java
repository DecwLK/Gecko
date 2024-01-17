package org.gecko.actions;

import org.gecko.viewmodel.Renamable;

public class RenameViewModelElementAction extends Action {
    private final Renamable elementToRename;
    private final String newName;

    RenameViewModelElementAction(Renamable renamable, String newName) {
        this.elementToRename = renamable;
        this.newName = newName;
    }

    @Override
    void run() {
        this.elementToRename.setName(this.newName);
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return null;
    }
}
