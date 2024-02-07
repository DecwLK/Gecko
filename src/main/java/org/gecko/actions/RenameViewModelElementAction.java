package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.viewmodel.AbstractViewModelElement;
import org.gecko.viewmodel.Renamable;

public class RenameViewModelElementAction extends Action {
    private final Renamable renamable;
    private final String oldName;
    private final String newName;

    RenameViewModelElementAction(Renamable renamable, String newName) {
        this.renamable = renamable;
        this.oldName = renamable.getName();
        this.newName = newName;
    }

    @Override
    boolean run() throws GeckoException {
        renamable.setName(newName);
        AbstractViewModelElement<?> abstractViewModelElement = (AbstractViewModelElement<?>) renamable;
        abstractViewModelElement.updateTarget();
        return true;
    }

    @Override
    Action getUndoAction(ActionFactory actionFactory) {
        return actionFactory.createRenameViewModelElementAction(this.renamable, this.oldName);
    }
}
