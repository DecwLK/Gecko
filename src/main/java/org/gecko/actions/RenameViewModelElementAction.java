package org.gecko.actions;

import org.gecko.exceptions.GeckoException;
import org.gecko.model.GeckoModel;
import org.gecko.viewmodel.AbstractViewModelElement;
import org.gecko.viewmodel.Renamable;

/**
 * A concrete representation of an {@link Action} that changes the name of a {@link Renamable}.  Additionally, holds the
 * old and new {@link String name}s of the element.
 */
public class RenameViewModelElementAction extends Action {
    private final GeckoModel geckoModel;
    private final Renamable renamable;
    private final String oldName;
    private final String newName;

    RenameViewModelElementAction(GeckoModel geckoModel, Renamable renamable, String newName) {
        this.renamable = renamable;
        this.oldName = renamable.getName();
        this.newName = newName;
        this.geckoModel = geckoModel;
    }

    @Override
    boolean run() throws GeckoException {
        if (!geckoModel.isNameUnique(newName)) {
            return false;
        }
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
